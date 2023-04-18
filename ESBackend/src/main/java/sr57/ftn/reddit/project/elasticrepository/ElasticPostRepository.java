package sr57.ftn.reddit.project.elasticrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

import java.util.List;

@Repository
public interface ElasticPostRepository extends ElasticsearchRepository<ElasticPost, Integer> {
    List<ElasticPost> findAllByTitle(String title);
    List<ElasticPost> findAllByText(String text);
    List<ElasticPost> findAll();
    List<ElasticPost> findAllByCommunity_Name(String name);
    List<ElasticPost> findAllByFlair_Name(String name);
}
