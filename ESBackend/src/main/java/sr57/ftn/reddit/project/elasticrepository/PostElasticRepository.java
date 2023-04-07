package sr57.ftn.reddit.project.elasticrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

import java.util.List;

public interface PostElasticRepository extends ElasticsearchRepository<ElasticPost, Integer> {
    List<ElasticPost> findAllByText(String text);
}
