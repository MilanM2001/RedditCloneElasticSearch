package sr57.ftn.reddit.project.elasticrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import sr57.ftn.reddit.project.model.elasticmodel.CommunityElastic;

import java.util.List;

public interface CommunityElasticRepository extends ElasticsearchRepository<CommunityElastic, Integer> {
    List<CommunityElastic> findAllByName(String name);
}
