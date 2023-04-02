package sr57.ftn.reddit.project.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", unique = true, nullable = false)
    private Integer comment_id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "timestamp", nullable = false)
    private LocalDate timestamp;

    @Column(name = "is_deleted", nullable = false)
    private Boolean is_deleted;

    //Comment has one User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Comment has one Post
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    //Comment has many Reactions
    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    private Set<Reaction> reactions = new HashSet<Reaction>();

    //Comment has many Reports
    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    private Set<Report> reports = new HashSet<Report>();
}
