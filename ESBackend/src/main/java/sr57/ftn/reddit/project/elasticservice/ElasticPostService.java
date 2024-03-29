package sr57.ftn.reddit.project.elasticservice;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.SimpleQueryEs;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs.ElasticPostDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs.ElasticPostResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper.ElasticPostMapper;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticFlair;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticUser;
import sr57.ftn.reddit.project.elasticrepository.ElasticPostRepository;
import sr57.ftn.reddit.project.lucene.indexing.handlers.DocumentHandler;
import sr57.ftn.reddit.project.lucene.indexing.handlers.PDFHandler;
import sr57.ftn.reddit.project.model.entity.Flair;
import sr57.ftn.reddit.project.model.entity.User;
import sr57.ftn.reddit.project.service.FlairService;
import sr57.ftn.reddit.project.service.PostService;
import sr57.ftn.reddit.project.service.UserService;
import sr57.ftn.reddit.project.util.SearchType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class ElasticPostService {

    @Value("${files.path}")
    private String filesPath;

    private final ElasticPostRepository elasticPostRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ElasticCommunityService elasticCommunityService;
    private final UserService userService;
    private final FlairService flairService;
    private final PostService postService;

    @Autowired
    public ElasticPostService(ElasticPostRepository elasticPostRepository,
                              ElasticsearchRestTemplate elasticsearchRestTemplate,
                              ElasticCommunityService elasticCommunityService,
                              UserService userService,
                              FlairService flairService, PostService postService) {
        this.elasticPostRepository = elasticPostRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
        this.elasticCommunityService = elasticCommunityService;
        this.userService = userService;
        this.flairService = flairService;
        this.postService = postService;
    }

    public void index(ElasticPost elasticPost) {
        elasticPostRepository.save(elasticPost);
    }

    public void indexUploadedFile(ElasticPostDTO elasticPostDTO) throws IOException {
        if (elasticPostDTO.getFiles() != null) {
            for (MultipartFile file : elasticPostDTO.getFiles()) {
                if (file.isEmpty()) {
                    continue;
                }

                String fileName = saveUploadedFileInFolder(file);
                if (fileName != null) {
                    ElasticPost postIndexUnit = getHandler(fileName).getIndexUnitPost(new File(fileName));
                    postIndexUnit.setPost_id(elasticPostDTO.getPost_id());
                    postIndexUnit.setTitle(elasticPostDTO.getTitle());
                    postIndexUnit.setText(elasticPostDTO.getText());
                    postIndexUnit.setKarma(1);
                    postIndexUnit.setNumberOfComments(0);
                    postIndexUnit.setComments(new HashSet<>());

                    User user = userService.findOne(elasticPostDTO.getUser_id());

                    ElasticUser elasticUser = new ElasticUser();
                    elasticUser.setUsername(user.getUsername());
                    postIndexUnit.setUser(elasticUser);

                    ElasticCommunity community = elasticCommunityService.findByCommunityId(elasticPostDTO.getCommunity_id());
                    postIndexUnit.setCommunity(community);

                    if (elasticPostDTO.getFlair_id() != 0) {
                        Flair flair = flairService.findOne(elasticPostDTO.getFlair_id());

                        ElasticFlair elasticFlair = new ElasticFlair();
                        elasticFlair.setFlair_id(flair.getFlair_id());
                        elasticFlair.setName(flair.getName());

                        postIndexUnit.setFlair(elasticFlair);
                    }

                    index(postIndexUnit);

                    Integer numberOfPosts = postService.countPostsByCommunityId(elasticPostDTO.getCommunity_id());
                    List<ElasticPost> elasticPosts = findAllByCommunityName(community.getName());
                    double totalKarma = 0.0;
                    for (ElasticPost eachElasticPost: elasticPosts) {
                        totalKarma += eachElasticPost.getKarma();
                    }

                    Double averageKarmaOfCommunity = totalKarma/numberOfPosts;

                    community.setNumberOfPosts(numberOfPosts);
                    community.setAverageKarma(averageKarmaOfCommunity);
                    elasticCommunityService.index(community);
                }
            }
        } else {
            ElasticPost postIndexUnit = new ElasticPost();
            postIndexUnit.setPost_id(elasticPostDTO.getPost_id());
            postIndexUnit.setTitle(elasticPostDTO.getTitle());
            postIndexUnit.setText(elasticPostDTO.getText());
            postIndexUnit.setKarma(1);
            postIndexUnit.setNumberOfComments(0);
            postIndexUnit.setComments(new HashSet<>());
            postIndexUnit.setPdfDescription(null);
            postIndexUnit.setFilename(null);

            User user = userService.findOne(elasticPostDTO.getUser_id());

            ElasticUser elasticUser = new ElasticUser();
            elasticUser.setUsername(user.getUsername());
            postIndexUnit.setUser(elasticUser);

            ElasticCommunity community = elasticCommunityService.findByCommunityId(elasticPostDTO.getCommunity_id());
            postIndexUnit.setCommunity(community);

            if (elasticPostDTO.getFlair_id() != 0) {
                Flair flair = flairService.findOne(elasticPostDTO.getFlair_id());

                ElasticFlair elasticFlair = new ElasticFlair();
                elasticFlair.setFlair_id(flair.getFlair_id());
                elasticFlair.setName(flair.getName());

                postIndexUnit.setFlair(elasticFlair);
            }

            index(postIndexUnit);

            Integer numberOfPosts = postService.countPostsByCommunityId(elasticPostDTO.getCommunity_id());
            List<ElasticPost> elasticPosts = findAllByCommunityName(community.getName());
            double totalKarma = 0.0;
            for (ElasticPost eachElasticPost: elasticPosts) {
                totalKarma += eachElasticPost.getKarma();
            }

            Double averageKarmaOfCommunity = totalKarma/numberOfPosts;

            community.setNumberOfPosts(numberOfPosts);
            community.setAverageKarma(averageKarmaOfCommunity);
            elasticCommunityService.index(community);
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

    public void update(ElasticPost elasticPost) {
        elasticPostRepository.save(elasticPost);
    }

    public void remove(Integer post_id) {
        elasticPostRepository.deleteById(post_id);
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

    public List<ElasticPostResponseDTO> findAllByTitle(String title, SearchType searchType) {
        QueryBuilder titleQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("title", title));

        BoolQueryBuilder boolQueryTitle = QueryBuilders
                .boolQuery()
                .must(titleQuery);

        return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitle));
    }

    public List<ElasticPostResponseDTO> findAllByText(String text, SearchType searchType) {
        QueryBuilder textQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("text", text));

        BoolQueryBuilder boolQueryText = QueryBuilders
                .boolQuery()
                .must(textQuery);

        return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryText));
    }

    public List<ElasticPostResponseDTO> findAllByPDFDescription(String pdfDescription, SearchType searchType) {
        QueryBuilder pdfDescriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("pdfDescription", pdfDescription));

        BoolQueryBuilder boolQueryPDFDescription = QueryBuilders
                .boolQuery()
                .must(pdfDescriptionQuery);

        return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryPDFDescription));
    }

    public List<ElasticPostResponseDTO> findAllByFlairName(String name, SearchType searchType) {
        QueryBuilder flairQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("flair.name", name));

        BoolQueryBuilder boolQueryTitle = QueryBuilders
                .boolQuery()
                .must(flairQuery);

        return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitle));
    }

    public List<ElasticPostResponseDTO> findAllByCommentsText(String text, SearchType searchType) {
        QueryBuilder textQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("comments.text", text));

        BoolQueryBuilder boolQueryTitle = QueryBuilders
                .boolQuery()
                .must(textQuery);

        return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitle));
    }

    public List<ElasticPostResponseDTO> findAllByKarma(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder karmaQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("karma", range));
        return ElasticPostMapper.mapDtos(searchBoolQuery(karmaQuery));
    }

    public List<ElasticPostResponseDTO> findAllByNumberOfComments(double from, double to) {
        String range = from + "-" + to;
        QueryBuilder numberOfCommentsQuery = SearchQueryGenerator.createRangeQueryBuilder(new SimpleQueryEs("numberOfComments", range));
        return ElasticPostMapper.mapDtos(searchBoolQuery(numberOfCommentsQuery));
    }

    public List<ElasticPostResponseDTO> findAllByTwoFields(String first, String second, Integer selectedFields, String boolQueryType, SearchType searchType) {
        //Title and Text
        if (selectedFields == 1) {
            QueryBuilder titleQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("title", first));
            QueryBuilder textQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("text", second));

            if (Objects.equals(boolQueryType, "OR")) {
                BoolQueryBuilder boolQueryTitleAndText = QueryBuilders
                        .boolQuery()
                        .should(titleQuery)
                        .should(textQuery);

                return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitleAndText));
            }
            else if (Objects.equals(boolQueryType, "AND")) {
                BoolQueryBuilder boolQueryTitleAndText = QueryBuilders
                        .boolQuery()
                        .must(titleQuery)
                        .must(textQuery);

                return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitleAndText));
            }
        }

        //Title and PDF Description
        if (selectedFields == 2) {
            QueryBuilder titleQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("title", first));
            QueryBuilder pdfDescriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("pdfDescription", second));

            if (Objects.equals(boolQueryType, "OR")) {
                BoolQueryBuilder boolQueryTitleAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .should(titleQuery)
                        .should(pdfDescriptionQuery);

                return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitleAndPDFDescription));
            }
            else if (Objects.equals(boolQueryType, "AND")) {
                BoolQueryBuilder boolQueryTitleAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .must(titleQuery)
                        .must(pdfDescriptionQuery);

                return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTitleAndPDFDescription));
            }
        }

        //Text and PDF Description -OR-
        if (selectedFields == 3) {
            QueryBuilder textQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("text", first));
            QueryBuilder pdfDescriptionQuery = SearchQueryGenerator.createMatchQueryBuilderTerm(searchType, new SimpleQueryEs("pdfDescription", second));

            if (Objects.equals(boolQueryType, "OR")) {
                BoolQueryBuilder boolQueryTextAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .should(textQuery)
                        .should(pdfDescriptionQuery);

                return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTextAndPDFDescription));
            }
            else if (Objects.equals(boolQueryType, "AND")) {
                BoolQueryBuilder boolQueryTextAndPDFDescription = QueryBuilders
                        .boolQuery()
                        .must(textQuery)
                        .must(pdfDescriptionQuery);

                return ElasticPostMapper.mapDtos(searchBoolQuery(boolQueryTextAndPDFDescription));
            }
        }
        return null;
    }

    private SearchHits<ElasticPost> searchBoolQuery(QueryBuilder boolQueryBuilder) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();

        return elasticsearchRestTemplate.search(searchQuery, ElasticPost.class, IndexCoordinates.of("posts"));
    }
}
