package sr57.ftn.reddit.project.model.dto.reactionDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.dto.userDTOs.SimpleInfoUserDTO;
import sr57.ftn.reddit.project.model.enums.ReactionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionForCommentAndPost {
    private Integer reaction_id;
    private ReactionType reaction_type;
    private SimpleInfoUserDTO user;
}
