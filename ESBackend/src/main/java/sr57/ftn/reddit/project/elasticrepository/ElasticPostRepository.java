package sr57.ftn.reddit.project.elasticrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

import java.util.List;

public interface ElasticPostRepository extends ElasticsearchRepository<ElasticPost, Integer> {
    List<ElasticPost> findAllByTitle(String title);
    List<ElasticPost> findAllByText(String text);
    List<ElasticPost> findAll();

}
