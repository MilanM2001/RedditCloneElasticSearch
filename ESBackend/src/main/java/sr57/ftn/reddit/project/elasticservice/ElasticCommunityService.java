package sr57.ftn.reddit.project.elasticservice;

import org.elasticsearch.index.query.QueryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.SimpleQueryEs;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper.ElasticCommunityMapper;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticrepository.ElasticCommunityRepository;

import java.util.List;

@Service
public class ElasticCommunityService {

    private final ElasticCommunityRepository elasticCommunityRepository;
    private final ModelMapper modelMapper;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    public ElasticCommunityService(ElasticCommunityRepository elasticCommunityRepository, ModelMapper modelMapper, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticCommunityRepository = elasticCommunityRepository;
        this.modelMapper = modelMapper;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public void index(ElasticCommunity elasticCommunity) {
        elasticCommunityRepository.save(elasticCommunity);
    }

    public void update(ElasticCommunity elasticCommunity) {
        elasticCommunityRepository.save(elasticCommunity);
    }

    public List<ElasticCommunity> findAll() {
        return elasticCommunityRepository.findAll();
    }

    public ElasticCommunity findByCommunityId(Integer community_id) {
        return elasticCommunityRepository.findById(community_id).orElseGet(null);
    }

    public List<ElasticCommunity> findAllByName(String name) {
        return elasticCommunityRepository.findAllByName(name);
    }

    public List<ElasticCommunity> findAllByDescription(String description) {
        return elasticCommunityRepository.findAllByDescription(description);
    }

    public List<ElasticCommunityResponseDTO> findByNumberOfPosts(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder numberOfPostsQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("numberOfPosts", range));
        return ElasticCommunityMapper.mapDtos(searchBoolQuery(numberOfPostsQuery));
    }

    public List<ElasticCommunityResponseDTO> findByAverageKarma(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder numberOfPostsQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("averageKarma", range));
        return ElasticCommunityMapper.mapDtos(searchBoolQuery(numberOfPostsQuery));
    }

    private SearchHits<ElasticCommunity> searchBoolQuery(QueryBuilder boolQueryBuilder) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();

        return elasticsearchRestTemplate.search(searchQuery, ElasticCommunity.class, IndexCoordinates.of("communities"));
    }
}
