package sr57.ftn.reddit.project.elasticservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.elasticrepository.PostElasticRepository;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

@Service
public class PostElasticService {

    private final PostElasticRepository postElasticRepository;

    @Autowired
    public PostElasticService(PostElasticRepository postElasticRepository) {
        this.postElasticRepository = postElasticRepository;
    }

    public void index(ElasticPost elasticPost) {
        postElasticRepository.save(elasticPost);
    }
}
