package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.elasticservice.CommunityElasticService;
import sr57.ftn.reddit.project.model.dto.communityDTOs.*;
import sr57.ftn.reddit.project.model.dto.flairDTOs.FlairDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.PostDTO;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.RuleDTO;
import sr57.ftn.reddit.project.model.elasticmodel.CommunityElastic;
import sr57.ftn.reddit.project.model.entity.*;
import sr57.ftn.reddit.project.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/communities")
public class CommunityController {
    final CommunityService communityService;
    final ModelMapper modelMapper;
    final UserService userService;
    final ModeratorService moderatorService;
    final RuleService ruleService;
    final FlairService flairService;
    final PostService postService;
    final ReactionService reactionService;
    final CommunityElasticService communityElasticService;

    @Autowired
    public CommunityController(CommunityService communityService, ModelMapper modelMapper,
                               UserService userService, ModeratorService moderatorService,
                               RuleService ruleService, FlairService flairService, PostService postService,
                               ReactionService reactionService, CommunityElasticService communityElasticService) {
        this.communityService = communityService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.moderatorService = moderatorService;
        this.ruleService = ruleService;
        this.flairService = flairService;
        this.postService = postService;
        this.reactionService = reactionService;
        this.communityElasticService = communityElasticService;
    }

    @GetMapping("/all")
    @CrossOrigin
    public ResponseEntity<List<CommunityDTO>> GetAll() {
        List<Community> communities = communityService.findAllNonSuspended();

        List<CommunityDTO> communitiesDTO = modelMapper.map(communities, new TypeToken<List<CommunityDTO>>() {
        }.getType());
        return new ResponseEntity<>(communitiesDTO, HttpStatus.OK);
    }

    @GetMapping("/single/{community_id}")
    public ResponseEntity<CommunityDTO> GetCommunity(@PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);
        return community == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(community, CommunityDTO.class), HttpStatus.OK);
    }

    @GetMapping(value = "/posts/{community_id}")
    public ResponseEntity<List<PostDTO>> GetCommunityPosts(@PathVariable Integer community_id) {
        List<Post> posts = postService.findPostsByCommunityId(community_id);

        List<PostDTO> postsDTO = modelMapper.map(posts, new TypeToken<List<PostDTO>>() {}.getType());
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/rules/{community_id}")
    public ResponseEntity<List<RuleDTO>> GetCommunityRules(@PathVariable Integer community_id) {
        List<Rule> rules = ruleService.findRulesByCommunityId(community_id);

        List<RuleDTO> rulesDTO = modelMapper.map(rules, new TypeToken<List<RuleDTO>>() {}.getType());
        return new ResponseEntity<>(rulesDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/flairs/{community_id}")
    public ResponseEntity<List<FlairDTO>> GetCommunityFlairs(@PathVariable Integer community_id) {
        List<Flair> flairs = flairService.findFlairsByCommunityId(community_id);

        List<FlairDTO> flairsDTO = modelMapper.map(flairs, new TypeToken<List<FlairDTO>>() {}.getType());
        return new ResponseEntity<>(flairsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/findByName/{name}")
    public ResponseEntity<List<CommunityElastic>> GetByName(@PathVariable String name) {
        List<CommunityElastic> communityByName = communityElasticService.findByName(name);
        return new ResponseEntity<>(communityByName, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddCommunityDTO> AddCommunity(@RequestBody AddCommunityDTO addCommunityDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Optional<Community> name = communityService.findFirstByName(addCommunityDTO.getName());

        if (name.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        Community newCommunity = modelMapper.map(addCommunityDTO, Community.class);

        newCommunity.setName(addCommunityDTO.getName());
        newCommunity.setDescription(addCommunityDTO.getDescription());
        newCommunity.setCreation_date(LocalDate.now());
        newCommunity.setIs_suspended(false);
        newCommunity.setSuspended_reason("Not Suspended");

//        Community savedCommunity = communityService.save(newCommunity);
          communityService.save(newCommunity);

        AddCommunityElasticDTO addCommunityElasticDTO = new AddCommunityElasticDTO();
        addCommunityElasticDTO.setName(addCommunityDTO.getName());
        addCommunityElasticDTO.setName(addCommunityDTO.getDescription());
          communityElasticService.save(addCommunityElasticDTO);

        for (RuleDTO ruleDTO : addCommunityDTO.getRules()) {
            Rule newRule = new Rule();
            newRule.setName(ruleDTO.getName());
            newRule.setDescription(ruleDTO.getDescription());
            newRule.setCommunity(newCommunity);
            ruleService.save(newRule);
        }

        for (FlairDTO flairDTO : addCommunityDTO.getFlairs()) {
            Flair newFlair = new Flair();
            newFlair.setName(flairDTO.getName());
            newFlair.setCommunity(newCommunity);
            flairService.save(newFlair);
        }

        Moderator newModerator = new Moderator();
        newModerator.setUser(user);
        newModerator.setCommunity(newCommunity);

        moderatorService.save(newModerator);
        return new ResponseEntity<>(modelMapper.map(newCommunity, AddCommunityDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{community_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateCommunityDTO> UpdateCommunity(@RequestBody UpdateCommunityDTO updateCommunityDTO, @PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        community.setDescription(updateCommunityDTO.getDescription());

        community = communityService.save(community);

        return new ResponseEntity<>(modelMapper.map(community, UpdateCommunityDTO.class), HttpStatus.OK);
    }

    //Only Admin can suspend a community
    @PutMapping(value = "/suspend/{community_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin
    public ResponseEntity<SuspendCommunityDTO> SuspendCommunity(@RequestBody SuspendCommunityDTO suspendCommunityDTO, @PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        community.setSuspended_reason(suspendCommunityDTO.getSuspended_reason());
        community.setIs_suspended(true);
//        community.setModerators(null);
        moderatorService.deleteByCommunityId(community_id);

        community = communityService.save(community);
        return new ResponseEntity<>(modelMapper.map(community, SuspendCommunityDTO.class), HttpStatus.OK);
    }

    //Not used
    @DeleteMapping(value = "/delete/{community_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> DeleteCommunity(@PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);

        if (community != null) {
            communityService.remove(community_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Android

    @PostMapping(value = "/addCommunityAndroid")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddCommunityDTOAndroid> AddCommunityAndroid(@RequestBody AddCommunityDTOAndroid addCommunityDTOAndroid, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Optional<Community> name = communityService.findFirstByName(addCommunityDTOAndroid.getName());

        if (name.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        Community newCommunity = modelMapper.map(addCommunityDTOAndroid, Community.class);

        newCommunity.setName(addCommunityDTOAndroid.getName());
        newCommunity.setDescription(addCommunityDTOAndroid.getDescription());
        newCommunity.setCreation_date(LocalDate.now());
        newCommunity.setIs_suspended(false);
        newCommunity.setSuspended_reason("Not Suspended");

        communityService.save(newCommunity);

        Moderator newModerator = new Moderator();
        newModerator.setUser(user);
        newModerator.setCommunity(newCommunity);

        moderatorService.save(newModerator);
        return new ResponseEntity<>(modelMapper.map(newCommunity, AddCommunityDTOAndroid.class), HttpStatus.CREATED);
    }

}
