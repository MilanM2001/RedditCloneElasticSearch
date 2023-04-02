package sr57.ftn.reddit.project.model.dto.communityDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommunityElasticDTO {
    private Integer community_id;
    private String name;
    private String description;
}
