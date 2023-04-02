package sr57.ftn.reddit.project.elasticservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.elasticrepository.CommunityElasticRepository;
import sr57.ftn.reddit.project.model.dto.communityDTOs.AddCommunityDTO;
import sr57.ftn.reddit.project.model.dto.communityDTOs.AddCommunityElasticDTO;
import sr57.ftn.reddit.project.model.elasticmodel.CommunityElastic;

import java.util.List;

@Service
public class CommunityElasticService {

    @Autowired
    private CommunityElasticRepository communityElasticRepository;

    @Autowired
    public CommunityElasticService(CommunityElasticRepository communityElasticRepository) {
        this.communityElasticRepository = communityElasticRepository;
    }

    public CommunityElastic save(AddCommunityElasticDTO addCommunityElasticDTO) {
        return communityElasticRepository.save(new CommunityElastic(123, addCommunityElasticDTO.getName(), addCommunityElasticDTO.getDescription()));
    }

    public List<CommunityElastic> findByName(String name) {
        return communityElasticRepository.findAllByName(name);
    }
}
