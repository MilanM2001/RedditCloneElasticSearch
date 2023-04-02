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
@Table(name = "post")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", unique = true, nullable = false)
    private Integer post_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creation_date;

    @Column(name = "image_path", nullable = false)
    private String image_path;

    //Post has one User
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Post has one Community
    @ManyToOne(targetEntity = Community.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id")
    private Community community;

    //Post has one or null Flair
    @ManyToOne(targetEntity = Flair.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "flair_id")
    private Flair flair;

    //Post has many Comments
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<Comment>();

    //Post has many Reactions
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Reaction> reactions = new HashSet<>();

    //Post has many Reports
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Report> reports = new HashSet<Report>();
}
