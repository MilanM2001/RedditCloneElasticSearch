package sr57.ftn.reddit.project.model.dto.userDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleInfoUserDTO {
    private Integer user_id;
    private String username;
    private String display_name;
    private String role;
}
