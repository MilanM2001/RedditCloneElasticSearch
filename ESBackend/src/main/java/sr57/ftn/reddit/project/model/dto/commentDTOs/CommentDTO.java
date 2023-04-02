package sr57.ftn.reddit.project.model.dto.commentDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionForCommentAndPost;
import sr57.ftn.reddit.project.model.dto.reportDTOs.ReportDTO;
import sr57.ftn.reddit.project.model.dto.userDTOs.SimpleInfoUserDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable {
    private Integer comment_id;
    private String text;
    private SimpleInfoUserDTO user;
    private Set<ReactionForCommentAndPost> reactions = new HashSet<>();
    private Set<ReportDTO> reports = new HashSet<>();
}
