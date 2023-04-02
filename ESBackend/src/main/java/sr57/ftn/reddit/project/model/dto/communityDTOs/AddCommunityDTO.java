package sr57.ftn.reddit.project.model.dto.communityDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.dto.flairDTOs.FlairDTO;
import sr57.ftn.reddit.project.model.dto.ruleDTOs.RuleDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommunityDTO implements Serializable {
    private String name;
    private String description;
    private Set<RuleDTO> rules = new HashSet<>();
    private Set<FlairDTO> flairs = new HashSet<>();
}
