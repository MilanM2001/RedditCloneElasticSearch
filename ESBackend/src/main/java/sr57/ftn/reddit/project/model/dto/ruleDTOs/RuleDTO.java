package sr57.ftn.reddit.project.model.dto.ruleDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuleDTO implements Serializable {
    private Integer rule_id;
    private String name;
    private String description;
}
