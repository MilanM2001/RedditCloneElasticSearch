package sr57.ftn.reddit.project.model.dto.userDTOs;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
