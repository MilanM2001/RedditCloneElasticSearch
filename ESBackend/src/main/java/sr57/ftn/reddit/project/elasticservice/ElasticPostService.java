package sr57.ftn.reddit.project.elasticservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.elasticrepository.ElasticPostRepository;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

import java.util.List;

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

    public void update(ElasticPost elasticPost) {
        elasticPostRepository.save(elasticPost);
    }

    public List<ElasticPost> findAll() {
        return elasticPostRepository.findAll();
    }

    public ElasticPost findByPostId(Integer post_id) {
        return elasticPostRepository.findById(post_id).orElseGet(null);
    }

    public List<ElasticPost> findAllByTitle(String title) {
        return elasticPostRepository.findAllByTitle(title);
    }

    public List<ElasticPost> findAllByText(String text) {
        return elasticPostRepository.findAllByText(text);
    }

    public void remove(Integer post_id) {
        elasticPostRepository.deleteById(post_id);
    }
}
