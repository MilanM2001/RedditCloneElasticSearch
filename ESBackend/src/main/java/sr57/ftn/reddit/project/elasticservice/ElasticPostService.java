package sr57.ftn.reddit.project.elasticservice;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.SimpleQueryEs;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs.ElasticPostResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper.ElasticCommunityMapper;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper.ElasticPostMapper;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;
import sr57.ftn.reddit.project.elasticrepository.ElasticPostRepository;
import sr57.ftn.reddit.project.util.SearchType;

import java.util.List;

@Service
public class ElasticPostService {

    private final ElasticPostRepository elasticPostRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    public ElasticPostService(ElasticPostRepository elasticPostRepository, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticPostRepository = elasticPostRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
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

    public List<ElasticPost> findAllByCommunityName(String name) {
        return elasticPostRepository.findAllByCommunity_Name(name);
    }

    public List<ElasticPost> findAllByFlairName(String name) {
        return elasticPostRepository.findAllByFlair_Name(name);
    }

    public List<ElasticPostResponseDTO> findAllByTitle(String title, SearchType searchType) {
        QueryBuilder titleQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("title", title));

        BoolQueryBuilder boolQueryTitle = QueryBuilders
                .boolQuery()
                .should(titleQuery);

        return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitle));
    }

    public List<ElasticPostResponseDTO> findAllByText(String text, SearchType searchType) {
        QueryBuilder textQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("text", text));

        BoolQueryBuilder boolQueryText = QueryBuilders
                .boolQuery()
                .should(textQuery);

        return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryText));
    }

    public void remove(Integer post_id) {
        elasticPostRepository.deleteById(post_id);
    }

    public List<ElasticPostResponseDTO> findByKarma(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder karmaQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("karma", range));
        return ElasticPostMapper.mapDtos(searchBoolQuery(karmaQuery));
    }

    public List<ElasticPostResponseDTO> findByNumberOfComments(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder numberOfCommentsQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("numberOfComments", range));
        return ElasticPostMapper.mapDtos(searchBoolQuery(numberOfCommentsQuery));
    }

    private SearchHits<ElasticPost> searchBoolQuery(QueryBuilder boolQueryBuilder) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();

        return elasticsearchRestTemplate.search(searchQuery, ElasticPost.class, IndexCoordinates.of("posts"));
    }
}
