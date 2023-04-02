package sr57.ftn.reddit.project.model.dto.ruleDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRuleDTO {
    private String name;
    private String description;
}
