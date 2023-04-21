package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class ElasticCommunityResponseDTO {
    private Integer community_id;
    private String name;
    private String description;
    private Integer numberOfPosts;
    private Double averageKarma;
    private String pdfDescription;
}
