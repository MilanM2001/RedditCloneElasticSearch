package sr57.ftn.reddit.project.model.dto.communityDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.dto.moderatorDTOs.ModeratorDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO implements Serializable {
    private Integer community_id;
    private String name;
    private String description;
    private Boolean is_suspended;
    private String suspended_reason;
    private Set<ModeratorDTO> moderators = new HashSet<>();
}
