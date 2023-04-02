package sr57.ftn.reddit.project.model.dto.flairDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlairDTO implements Serializable {
    private Integer flair_id;
    private String name;
}
