package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs;

import lombok.Builder;
import lombok.Data;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticUser;

@Data
@Builder
public class ElasticPostResponseDTO {
    private Integer post_id;
    private String title;
    private String text;
    private Integer karma;
    private ElasticCommunity community;
    private ElasticUser user;
}
