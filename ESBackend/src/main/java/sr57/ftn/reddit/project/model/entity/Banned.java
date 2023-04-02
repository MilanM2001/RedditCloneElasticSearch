package sr57.ftn.reddit.project.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "banned")
public class Banned implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banned_id", unique = true, nullable = false)
    private Integer banned_id;

    @Column(name = "timestamp", nullable = false)
    private LocalDate timestamp;

    //Banned has one Community
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id")
    private Community community;

    //Banned has one User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    //Banned has one Moderator
    /*@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "moderator_id", nullable = false)
    private Moderator moderator;*/
}
