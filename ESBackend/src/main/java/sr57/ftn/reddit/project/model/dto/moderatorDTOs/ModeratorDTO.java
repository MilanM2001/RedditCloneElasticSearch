package sr57.ftn.reddit.project.model.dto.moderatorDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeratorDTO implements Serializable {
    private Integer user_id;
}
