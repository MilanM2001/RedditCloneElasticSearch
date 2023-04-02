package sr57.ftn.reddit.project.model.dto.postDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.dto.communityDTOs.CommunityDTO;
import sr57.ftn.reddit.project.model.dto.flairDTOs.FlairDTO;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionForCommentAndPost;
import sr57.ftn.reddit.project.model.dto.reportDTOs.ReportDTO;
import sr57.ftn.reddit.project.model.dto.userDTOs.UserDTO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {
    private Integer post_id;
    private String title;
    private String text;
    private String image_path;
    private Set<ReactionForCommentAndPost> reactions = new HashSet<>();
    private CommunityDTO community;
    private UserDTO user;
    private FlairDTO flair;
}
