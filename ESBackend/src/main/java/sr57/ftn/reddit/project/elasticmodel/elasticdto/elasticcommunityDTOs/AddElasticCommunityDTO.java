package sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticcommunityDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddElasticCommunityDTO {
    private Integer community_id;
    private String name;
    private String description;
    private Boolean is_suspended;
}
