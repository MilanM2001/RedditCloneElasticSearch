package sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper;

import org.springframework.data.elasticsearch.core.SearchHits;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs.ElasticPostResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;

import java.util.List;

public class ElasticPostMapper {

    public static ElasticPostResponseDTO mapResponseDto(ElasticPost elasticPost) {
        return ElasticPostResponseDTO.builder()
                .post_id(elasticPost.getPost_id())
                .title(elasticPost.getTitle())
                .text(elasticPost.getText())
                .karma(elasticPost.getKarma())
                .community(elasticPost.getCommunity())
                .user(elasticPost.getUser())
                .build();
    }

    public static List<ElasticPostResponseDTO> mapDtos(SearchHits<ElasticPost> searchHits) {
        return searchHits
                .map(elasticPost -> mapResponseDto(elasticPost.getContent()))
                .toList();
    }
}
