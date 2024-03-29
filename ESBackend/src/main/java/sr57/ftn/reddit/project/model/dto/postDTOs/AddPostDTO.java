package sr57.ftn.reddit.project.model.dto.postDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddPostDTO implements Serializable {
    private Integer post_id;
    private String title;
    private String text;
    @Nullable
    private int flair_id;
}
