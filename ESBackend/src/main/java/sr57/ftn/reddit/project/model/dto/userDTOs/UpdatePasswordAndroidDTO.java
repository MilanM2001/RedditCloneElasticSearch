package sr57.ftn.reddit.project.model.dto.userDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordAndroidDTO {
    @NotBlank
    private String newPassword;
    @NotBlank
    private String oldPassword;
}
