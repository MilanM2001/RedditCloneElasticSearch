package sr57.ftn.reddit.project.elasticservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.elasticrepository.ElasticPostRepository;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

@Service
public class ElasticPostService {

    private final ElasticPostRepository elasticPostRepository;

    @Autowired
    public ElasticPostService(ElasticPostRepository elasticPostRepository) {
        this.elasticPostRepository = elasticPostRepository;
    }

    public void index(ElasticPost elasticPost) {
        elasticPostRepository.save(elasticPost);
    }
}
