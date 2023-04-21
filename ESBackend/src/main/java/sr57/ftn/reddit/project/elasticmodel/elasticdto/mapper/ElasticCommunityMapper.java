package sr57.ftn.reddit.project.elasticmodel.elasticdto.mapper;

import org.springframework.data.elasticsearch.core.SearchHits;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs.ElasticCommunityResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;

import java.util.List;

public class ElasticCommunityMapper {

    public static ElasticCommunityResponseDTO mapResponseDto(ElasticCommunity elasticCommunity) {
        return ElasticCommunityResponseDTO.builder()
                .community_id(elasticCommunity.getCommunity_id())
                .name(elasticCommunity.getName())
                .description(elasticCommunity.getDescription())
                .numberOfPosts(elasticCommunity.getNumberOfPosts())
                .averageKarma(elasticCommunity.getAverageKarma())
                .pdfDescription(elasticCommunity.getPdfDescription())
                .build();
    }

    public static List<ElasticCommunityResponseDTO> mapDtos(SearchHits<ElasticCommunity> searchHits) {
        return searchHits
                .map(elasticCommunity -> mapResponseDto(elasticCommunity.getContent()))
                .toList();
    }
}
