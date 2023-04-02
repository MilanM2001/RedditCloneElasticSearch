package sr57.ftn.reddit.project.model.dto.postDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostDTO implements Serializable {
    private String title;
    private String text;
}
