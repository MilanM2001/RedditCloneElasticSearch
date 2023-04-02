package sr57.ftn.reddit.project.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "moderator")
public class Moderator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moderator_id", unique = true, nullable = false)
    private Integer moderator_id;

    //User who has Moderator Role
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Communities which he is Moderating
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;
}
