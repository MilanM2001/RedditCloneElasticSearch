package sr57.ftn.reddit.project.elasticservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.elasticrepository.CommunityElasticRepository;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.AddElasticCommunityDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;

import java.util.List;

@Service
public class CommunityElasticService {

    private final CommunityElasticRepository communityElasticRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommunityElasticService(CommunityElasticRepository communityElasticRepository, ModelMapper modelMapper) {
        this.communityElasticRepository = communityElasticRepository;
        this.modelMapper = modelMapper;
    }

    public void index(AddElasticCommunityDTO addElasticCommunityDTO) {
        ElasticCommunity elasticCommunity;
        elasticCommunity = modelMapper.map(addElasticCommunityDTO, ElasticCommunity.class);
        communityElasticRepository.save(elasticCommunity);
    }

    public void update(ElasticCommunity elasticCommunity) {
        communityElasticRepository.save(elasticCommunity);
    }

    public List<ElasticCommunity> findAllByDescription(String description) {
        return communityElasticRepository.findAllByDescription(description);
    }

    public List<ElasticCommunity> findAllByName(String name) {
        return communityElasticRepository.findAllByName(name);
    }

    public ElasticCommunity findOneByName(String name) {
        return communityElasticRepository.findByName(name);
    }

    public ElasticCommunity findByCommunityId(Integer community_id) {
        return communityElasticRepository.findById(community_id).orElseGet(null);
    }
}
