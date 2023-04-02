package sr57.ftn.reddit.project.model.entity;

import lombok.*;
import sr57.ftn.reddit.project.model.enums.ReactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reaction")
public class Reaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id", unique = true, nullable = false)
    private Integer reaction_id;

    @Column(name = "reaction_type", unique = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private ReactionType reaction_type;

    @Column(name = "timestamp", unique = false, nullable = false)
    private LocalDate timestamp;

    //Reaction has one Comment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;

    //Reaction has one Post
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    //Reaction has one User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
