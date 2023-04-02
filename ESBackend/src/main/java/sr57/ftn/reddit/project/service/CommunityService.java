package sr57.ftn.reddit.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Community;
import sr57.ftn.reddit.project.repository.CommunityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {

    final CommunityRepository communityRepository;

    final RuleService ruleService;

    @Autowired
    public CommunityService(CommunityRepository communityRepository, RuleService ruleService) {
        this.communityRepository = communityRepository;
        this.ruleService = ruleService;
    }

    public Community findOne(Integer id) {
        return communityRepository.findById(id).orElseGet(null);
    }

    public Optional<Community> findFirstByName(String name) {
        return communityRepository.findFirstByName(name);
    }

    public List<Community> findAll() {
        return communityRepository.findAll();
    }

    public List<Community> findAllNonSuspended() {
        return communityRepository.findAllNonSuspended();
    }

    public void remove(Integer id) {
        communityRepository.deleteById(id);
    }

    public Community save(Community community) {
        return communityRepository.save(community);
    }
}
