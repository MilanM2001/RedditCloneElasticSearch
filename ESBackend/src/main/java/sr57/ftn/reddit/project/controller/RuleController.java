package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.AddRuleDTO;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.RuleDTO;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.UpdateRuleDTO;
import sr57.ftn.reddit.project.model.entity.Rule;
import sr57.ftn.reddit.project.service.CommunityService;
import sr57.ftn.reddit.project.service.RuleService;

import java.util.List;

@RestController
@RequestMapping("api/rules")
public class RuleController {
    final RuleService ruleService;
    final CommunityService communityService;
    final ModelMapper modelMapper;

    @Autowired
    public RuleController(RuleService ruleService, CommunityService communityService, ModelMapper modelMapper) {
        this.ruleService = ruleService;
        this.communityService = communityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/single/{rule_id}")
    public ResponseEntity<RuleDTO> getRule(@PathVariable("rule_id") Integer rule_id) {
        Rule rule = ruleService.findOne(rule_id);
        return rule == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(rule, RuleDTO.class), HttpStatus.OK);
    }

    @PostMapping(value = "/add/{community_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddRuleDTO> AddRule(@PathVariable("community_id") Integer community_id, @RequestBody AddRuleDTO addRuleDTO) {
        Rule newRule = new Rule();

        newRule.setName(addRuleDTO.getName());
        newRule.setDescription(addRuleDTO.getDescription());
        newRule.setCommunity(communityService.findOne(community_id));

        newRule = ruleService.save(newRule);
        return new ResponseEntity<>(modelMapper.map(newRule, AddRuleDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{rule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateRuleDTO> UpdateRule(@RequestBody UpdateRuleDTO updateRuleDTO, @PathVariable("rule_id") Integer rule_id) {
        Rule rule = ruleService.findOne(rule_id);

        if (rule == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        rule.setName(updateRuleDTO.getName());
        rule.setDescription(updateRuleDTO.getDescription());

        rule = ruleService.save(rule);
        return new ResponseEntity<>(modelMapper.map(rule, UpdateRuleDTO.class), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{rule_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteRule(@PathVariable("rule_id") Integer rule_id) {
        Rule rule = ruleService.findOne(rule_id);

        if (rule != null) {
            ruleService.remove(rule_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
