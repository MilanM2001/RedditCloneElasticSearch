package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs;

import lombok.*;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticRule;

import java.util.Set;

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
    private Set<ElasticRule> rules;
}
