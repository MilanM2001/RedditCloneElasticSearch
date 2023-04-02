package sr57.ftn.reddit.project.model.dto.userDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionDTO;
import sr57.ftn.reddit.project.model.entity.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer user_id;
    private String username;
    private String email;
    private String avatar;
    private String description;
    private String display_name;
    private String role;
    private String token;
    private Set<ReactionDTO> reactions = new HashSet<>();

    public UserDTO(User user) {
        this.user_id = user.getUser_id();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.description = user.getDescription();
        this.display_name = user.getDisplay_name();
        this.role = user.getRole();
    }
}
