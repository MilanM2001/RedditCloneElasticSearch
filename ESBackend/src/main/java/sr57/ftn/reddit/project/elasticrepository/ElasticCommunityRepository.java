package sr57.ftn.reddit.project.elasticrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;

import java.util.List;

public interface ElasticCommunityRepository extends ElasticsearchRepository<ElasticCommunity, Integer> {
    List<ElasticCommunity> findAllByDescription(String description);
    List<ElasticCommunity> findAllByName(String name);
}
