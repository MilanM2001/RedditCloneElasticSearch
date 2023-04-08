package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElasticCommunityResponseDTO {
    private Integer community_id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
