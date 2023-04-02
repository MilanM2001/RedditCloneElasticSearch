package sr57.ftn.reddit.project.model.dto.userDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String display_name;
}
