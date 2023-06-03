package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs.ElasticPostResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;
import sr57.ftn.reddit.project.elasticservice.ElasticCommunityService;
import sr57.ftn.reddit.project.elasticservice.ElasticPostService;
import sr57.ftn.reddit.project.model.dto.communityDTOs.AddCommunityDTO;
import sr57.ftn.reddit.project.model.dto.communityDTOs.CommunityDTO;
import sr57.ftn.reddit.project.model.dto.communityDTOs.SuspendCommunityDTO;
import sr57.ftn.reddit.project.model.dto.communityDTOs.UpdateCommunityDTO;
import sr57.ftn.reddit.project.model.dto.flairDTOs.FlairDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.PostDTO;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.RuleDTO;
import sr57.ftn.reddit.project.model.entity.Community;
import sr57.ftn.reddit.project.model.entity.Flair;
import sr57.ftn.reddit.project.model.entity.Post;
import sr57.ftn.reddit.project.model.entity.Rule;
import sr57.ftn.reddit.project.service.*;
import sr57.ftn.reddit.project.util.SearchType;

import java.io.IOException;
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
    final ElasticCommunityService elasticCommunityService;
    final ElasticPostService elasticPostService;

    @Autowired
    public CommunityController(CommunityService communityService, ModelMapper modelMapper,
                               UserService userService, ModeratorService moderatorService,
                               RuleService ruleService, FlairService flairService, PostService postService,
                               ReactionService reactionService, ElasticCommunityService elasticCommunityService,
                               ElasticPostService elasticPostService) {
        this.communityService = communityService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.moderatorService = moderatorService;
        this.ruleService = ruleService;
        this.flairService = flairService;
        this.postService = postService;
        this.reactionService = reactionService;
        this.elasticCommunityService = elasticCommunityService;
        this.elasticPostService = elasticPostService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommunityDTO>> GetAll() {
        List<Community> communities = communityService.findAllNonSuspended();
        List<CommunityDTO> communitiesDTO = modelMapper.map(communities, new TypeToken<List<CommunityDTO>>() {
        }.getType());

        return new ResponseEntity<>(communitiesDTO, HttpStatus.OK);
    }

    @GetMapping("/allElastic")
    public ResponseEntity<List<ElasticCommunity>> GetAllElastic() {
        List<ElasticCommunity> elasticCommunities = elasticCommunityService.findAll();

        return new ResponseEntity<>(elasticCommunities, HttpStatus.OK);
    }

    @GetMapping("/single/{community_id}")
    public ResponseEntity<CommunityDTO> GetCommunity(@PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);
        CommunityDTO communityDTO = modelMapper.map(community, CommunityDTO.class);

        return new ResponseEntity<>(communityDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/findAllByName/{name}/{searchType}")
    public ResponseEntity<List<ElasticCommunityResponseDTO>> GetAllByNameQuery(@PathVariable String name, @PathVariable String searchType) {
        SearchType search = SearchType.valueOf(searchType);
        List<ElasticCommunityResponseDTO> elasticCommunities = elasticCommunityService.findAllByName(name, search);

        return new ResponseEntity<>(elasticCommunities, HttpStatus.OK);
    }

    @GetMapping(value = "/findAllByDescription/{description}/{searchType}")
    public ResponseEntity<List<ElasticCommunityResponseDTO>> GetAllByDescription(@PathVariable String description, @PathVariable String searchType) {
        SearchType search = SearchType.valueOf(searchType);
        List<ElasticCommunityResponseDTO> elasticCommunities = elasticCommunityService.findAllByDescription(description, search);

        return new ResponseEntity<>(elasticCommunities, HttpStatus.OK);
    }

    @GetMapping(value = "/findAllByPDFDescription/{pdfDescription}/{searchType}")
    public ResponseEntity<List<ElasticCommunityResponseDTO>> GetAllByPDFDescription(@PathVariable String pdfDescription, @PathVariable String searchType) {
        SearchType search = SearchType.valueOf(searchType);
        List<ElasticCommunityResponseDTO> elasticCommunities = elasticCommunityService.findAllByPDFDescription(pdfDescription, search);

        return new ResponseEntity<>(elasticCommunities, HttpStatus.OK);
    }

    @GetMapping(value = "/findAllByRulesDescription/{description}/{searchType}")
    public ResponseEntity<List<ElasticCommunityResponseDTO>> GetAllByCommentsText(@PathVariable String description, @PathVariable String searchType) {
        SearchType search = SearchType.valueOf(searchType);
        List<ElasticCommunityResponseDTO> elasticPosts = elasticCommunityService.findAllByRuleDescription(description, search);

        return new ResponseEntity<>(elasticPosts, HttpStatus.OK);
    }

    @GetMapping(value = "/findAllByTwoFields/{first}/{second}/{selectedFields}/{boolQueryType}/{searchType}")
    public ResponseEntity<List<ElasticCommunityResponseDTO>> GetAllByTwoFields(@PathVariable String first, @PathVariable String second, @PathVariable Integer selectedFields,
                                                                               @PathVariable String boolQueryType, @PathVariable String searchType) {
        SearchType search = SearchType.valueOf(searchType);
        List<ElasticCommunityResponseDTO> elasticCommunities = elasticCommunityService.findAllByTwoFields(first, second, selectedFields, boolQueryType, search);

        return new ResponseEntity<>(elasticCommunities, HttpStatus.OK);
    }

    @GetMapping("/numberOfPosts/{from}/to/{to}")
    public ResponseEntity<List<ElasticCommunityResponseDTO>> GetByNumberOfPostsRange(@PathVariable Integer from, @PathVariable Integer to) {
        List<ElasticCommunityResponseDTO> elasticCommunities = elasticCommunityService.findByNumberOfPosts(from, to);

        return new ResponseEntity<>(elasticCommunities, HttpStatus.OK);
    }

    @GetMapping("/averageKarma/{from}/to/{to}")
    public ResponseEntity<List<ElasticCommunityResponseDTO>> GetByAverageKarmaRange(@PathVariable Double from, @PathVariable Double to) {
        List<ElasticCommunityResponseDTO> elasticCommunities = elasticCommunityService.findByAverageKarma(from, to);

        return new ResponseEntity<>(elasticCommunities, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/{community_id}")
    public ResponseEntity<List<PostDTO>> GetCommunityPosts(@PathVariable Integer community_id) {
        List<Post> posts = postService.findPostsByCommunityId(community_id);
        List<PostDTO> postsDTO = modelMapper.map(posts, new TypeToken<List<PostDTO>>() {}.getType());

        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/elasticPosts/{name}")
    public ResponseEntity<List<ElasticPost>> GetAllByCommunityName(@PathVariable("name") String name) {
        List<ElasticPost> elasticPosts = elasticPostService.findAllByCommunityName(name);

        return new ResponseEntity<>(elasticPosts, HttpStatus.OK);
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

    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddCommunityDTO> AddCommunity(@RequestBody AddCommunityDTO addCommunityDTO, Authentication authentication) {
//        User user = userService.findByUsername(authentication.getName());
        Optional<Community> name = communityService.findFirstByName(addCommunityDTO.getName());

        if (name.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Community newCommunity = modelMapper.map(addCommunityDTO, Community.class);

        newCommunity.setName(addCommunityDTO.getName());
        newCommunity.setDescription(addCommunityDTO.getDescription());
        newCommunity.setCreation_date(LocalDate.now());
        newCommunity.setIs_suspended(false);
        newCommunity.setSuspended_reason("Not Suspended");
        communityService.save(newCommunity);

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

//        Moderator newModerator = new Moderator();
//        newModerator.setUser(user);
//        newModerator.setCommunity(newCommunity);
//        moderatorService.save(newModerator);

        return new ResponseEntity<>(modelMapper.map(newCommunity, AddCommunityDTO.class), HttpStatus.CREATED);
    }

    @PostMapping(path = "/pdf", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public void uploadPDF(@ModelAttribute ElasticCommunityDTO uploadModel) throws IOException {
        elasticCommunityService.indexUploadedFile(uploadModel);
    }

    @PutMapping(value = "/update/{community_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateCommunityDTO> UpdateCommunity(@RequestBody UpdateCommunityDTO updateCommunityDTO, @PathVariable("community_id") Integer community_id) {
        Community community = communityService.findOne(community_id);
        ElasticCommunity elasticCommunity = elasticCommunityService.findByCommunityId(community_id);

        if (community == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (elasticCommunity == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        community.setDescription(updateCommunityDTO.getDescription());
        communityService.save(community);

        elasticCommunity.setDescription(updateCommunityDTO.getDescription());
        elasticCommunityService.index(elasticCommunity);

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


    @DeleteMapping(value = "/delete/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> DeleteCommunity(@PathVariable("name") String name) {
        ElasticCommunity community = elasticCommunityService.findOneByName(name);

        if (community != null) {
            communityService.remove(community.getCommunity_id());
            elasticCommunityService.deleteByName(name);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
