package sr57.ftn.reddit.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Rule;
import sr57.ftn.reddit.project.repository.RuleRepository;

import java.util.List;

@Service
public class RuleService {
    final RuleRepository ruleRepository;

    @Autowired
    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Rule findOne(Integer id) {
        return ruleRepository.findById(id).orElseGet(null);
    }

    public List<Rule> findAll() {
        return ruleRepository.findAll();
    }

    public void remove(Integer id) {
        ruleRepository.deleteById(id);
    }

    public Rule save(Rule rule) {
        return ruleRepository.save(rule);
    }

    public List<Rule> findRulesByCommunityId(Integer community_id) {
        return ruleRepository.findRulesByCommunityId(community_id);
    }
}
