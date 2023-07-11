package sr57.ftn.reddit.project.elasticservice;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.multipart.MultipartFile;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.SimpleQueryEs;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs.ElasticPostResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper.ElasticCommunityMapper;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper.ElasticPostMapper;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticrepository.ElasticCommunityRepository;
import sr57.ftn.reddit.project.lucene.indexing.handlers.DocumentHandler;
import sr57.ftn.reddit.project.lucene.indexing.handlers.PDFHandler;
import sr57.ftn.reddit.project.util.SearchType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ElasticCommunityService {

    @Value("${files.path}")
    private String filesPath;

    private final ElasticCommunityRepository elasticCommunityRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    public ElasticCommunityService(ElasticCommunityRepository elasticCommunityRepository, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticCommunityRepository = elasticCommunityRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public void index(ElasticCommunity elasticCommunity) {
        elasticCommunityRepository.save(elasticCommunity);
    }

    public void indexUploadedFile(ElasticCommunityDTO elasticCommunityDTO) throws IOException {
        if (elasticCommunityDTO.getFiles() != null) {
            for (MultipartFile file : elasticCommunityDTO.getFiles()) {
                if (file.isEmpty()) {
                    continue;
                }

                String fileName = saveUploadedFileInFolder(file);
                if (fileName != null) {
                    ElasticCommunity communityIndexUnit = getHandler(fileName).getIndexUnitCommunity(new File(fileName));
                    communityIndexUnit.setCommunity_id(elasticCommunityDTO.getCommunity_id());
                    communityIndexUnit.setName(elasticCommunityDTO.getName());
                    communityIndexUnit.setDescription(elasticCommunityDTO.getDescription());
                    communityIndexUnit.setNumberOfPosts(0);
                    communityIndexUnit.setAverageKarma(0.0);
                    communityIndexUnit.setRules(new HashSet<>());
                    index(communityIndexUnit);
                }
            }
        } else {
            ElasticCommunity communityIndexUnit = new ElasticCommunity();
            communityIndexUnit.setCommunity_id(elasticCommunityDTO.getCommunity_id());
            communityIndexUnit.setName(elasticCommunityDTO.getName());
            communityIndexUnit.setDescription(elasticCommunityDTO.getDescription());
            communityIndexUnit.setNumberOfPosts(0);
            communityIndexUnit.setAverageKarma(0.0);
            communityIndexUnit.setPdfDescription(null);
            communityIndexUnit.setFilename(null);
            communityIndexUnit.setRules(new HashSet<>());
            index(communityIndexUnit);
        }
    }

    public DocumentHandler getHandler(String fileName) {
        return getDocumentHandler(fileName);
    }

    public static DocumentHandler getDocumentHandler(String fileName) {
        if (fileName.endsWith(".pdf")) {
            return new PDFHandler();
        } else {
            return null;
        }
    }

    private String saveUploadedFileInFolder(MultipartFile file) throws IOException {
        String retVal = null;
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(new File(filesPath).getAbsolutePath() + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);
            retVal = path.toString();
        }
        return retVal;
    }

    public List<ElasticCommunity> findAll() {
        return elasticCommunityRepository.findAll();
    }

    public ElasticCommunity findByCommunityId(Integer community_id) {
        return elasticCommunityRepository.findById(community_id).orElseGet(null);
    }

    public List<ElasticCommunityResponseDTO> findAllByName(String name, SearchType searchType) {
        QueryBuilder nameQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("name", name));

        BoolQueryBuilder boolQueryName = QueryBuilders
                .boolQuery()
                .must(nameQuery);

        return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryName));
    }

    public List<ElasticCommunityResponseDTO> findAllByDescription(String description, SearchType searchType) {
        QueryBuilder descriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("description", description));

        BoolQueryBuilder boolQueryDescription = QueryBuilders
                .boolQuery()
                .must(descriptionQuery);

        return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryDescription));
    }

    public List<ElasticCommunityResponseDTO> findAllByPDFDescription(String pdfDescription, SearchType searchType) {
        QueryBuilder pdfDescriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("pdfDescription", pdfDescription));

        BoolQueryBuilder boolQueryPDFDescription = QueryBuilders
                .boolQuery()
                .must(pdfDescriptionQuery);

        return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryPDFDescription));
    }

    public List<ElasticCommunityResponseDTO> findAllByRuleDescription(String description, SearchType searchType) {
        QueryBuilder nameQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("rules.description", description));

        BoolQueryBuilder boolQueryTitle = QueryBuilders
                .boolQuery()
                .must(nameQuery);

        return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryTitle));
    }

    public List<ElasticCommunityResponseDTO> findByNumberOfPosts(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder numberOfPostsQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("numberOfPosts", range));
        return ElasticCommunityMapper.mapDtos(searchBoolQuery(numberOfPostsQuery));
    }

    public List<ElasticCommunityResponseDTO> findByAverageKarma(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder averageKarmaQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("averageKarma", range));
        return ElasticCommunityMapper.mapDtos(searchBoolQuery(averageKarmaQuery));
    }

    public List<ElasticCommunityResponseDTO> findAllByTwoFields(String first, String second, Integer selectedFields, String boolQueryType, SearchType searchType) {
        //Name and Description
        if (selectedFields == 1) {
            QueryBuilder nameQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("name", first));
            QueryBuilder descriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("description", second));

            if (Objects.equals(boolQueryType, "OR")) {
                BoolQueryBuilder boolQueryNameAndDescription = QueryBuilders
                        .boolQuery()
                        .should(nameQuery)
                        .should(descriptionQuery);

                return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryNameAndDescription));
            }
            else if (Objects.equals(boolQueryType, "AND")) {
                BoolQueryBuilder boolQueryNameAndDescription = QueryBuilders
                        .boolQuery()
                        .must(nameQuery)
                        .must(descriptionQuery);

                return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryNameAndDescription));
            }
        }

        //Name and PDF Description
        if (selectedFields == 2) {
            QueryBuilder nameQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("name", first));
            QueryBuilder pdfDescriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("pdfDescription", second));

            if (Objects.equals(boolQueryType, "OR")) {
                BoolQueryBuilder boolQueryNameAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .should(nameQuery)
                        .should(pdfDescriptionQuery);

                return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryNameAndPDFDescription));
            }
            else if (Objects.equals(boolQueryType, "AND")) {
                BoolQueryBuilder boolQueryNameAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .must(nameQuery)
                        .must(pdfDescriptionQuery);

                return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryNameAndPDFDescription));
            }
        }

        //Description and PDF Description -OR-
        if (selectedFields == 3) {
            QueryBuilder descriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("description", first));
            QueryBuilder pdfDescriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("pdfDescription", second));

            if (Objects.equals(boolQueryType, "OR")) {
                BoolQueryBuilder boolQueryDescriptionAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .should(descriptionQuery)
                        .should(pdfDescriptionQuery);

                return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryDescriptionAndPDFDescription));
            }
            else if (Objects.equals(boolQueryType, "AND")) {
                BoolQueryBuilder boolQueryDescriptionAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .must(descriptionQuery)
                        .must(pdfDescriptionQuery);

                return ElasticCommunityMapper.mapDtos(searchBoolQuery(boolQueryDescriptionAndPDFDescription));
            }
        }
        return null;
    }

    public void deleteByName(String name) {
        elasticCommunityRepository.deleteByName(name);
    }

    public ElasticCommunity findOneByName(String name) {
        return elasticCommunityRepository.findByName(name);
    }

    private SearchHits<ElasticCommunity> searchBoolQuery(QueryBuilder queryBuilder) {

        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("name")
                .field("description")
                .field("pdfDescription")
                .preTags("<strong>")
                .postTags("</strong>");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .build();

        return elasticsearchRestTemplate.search(searchQuery, ElasticCommunity.class, IndexCoordinates.of("communities"));
    }
}
