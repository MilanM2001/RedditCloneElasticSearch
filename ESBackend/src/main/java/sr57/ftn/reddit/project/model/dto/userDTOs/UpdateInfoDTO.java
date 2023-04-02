package sr57.ftn.reddit.project.model.dto.userDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInfoDTO {
    private String display_name;
    private String description;
}
