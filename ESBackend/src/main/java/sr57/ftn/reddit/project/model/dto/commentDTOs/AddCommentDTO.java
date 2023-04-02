package sr57.ftn.reddit.project.model.dto.commentDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentDTO implements Serializable {
    private String text;
}
