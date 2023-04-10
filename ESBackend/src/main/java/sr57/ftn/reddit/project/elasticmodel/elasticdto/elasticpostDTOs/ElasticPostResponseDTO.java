package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElasticPostResponseDTO {
    private Integer post_id;
    private String title;
    private String text;
    private Integer karma;
}
