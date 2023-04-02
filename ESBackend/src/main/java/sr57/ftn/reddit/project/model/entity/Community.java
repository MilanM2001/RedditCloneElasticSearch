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
@Table(name = "community")
public class Community implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id", unique = true, nullable = false)
    private Integer community_id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creation_date;

    @Column(name = "is_suspended", nullable = false)
    private Boolean is_suspended;

    @Column(name = "suspended_reason", nullable = false)
    private String suspended_reason;

    //Community has many Posts
    @OneToMany(mappedBy = "community", fetch = FetchType.EAGER)
    private Set<Post> posts = new HashSet<>();

    //Community has many Flairs
    @OneToMany(mappedBy = "community", fetch = FetchType.EAGER)
    private Set<Flair> flairs = new HashSet<>();

    @OneToMany(mappedBy = "community", fetch = FetchType.EAGER)
    private Set<Banned> bans = new HashSet<>();

    //Community has many Rules
    @OneToMany(mappedBy = "community", fetch = FetchType.EAGER)
    private Set<Rule> rules = new HashSet<>();

    //Community can be Moderated by multiple people????
    @OneToMany(mappedBy = "community", fetch = FetchType.EAGER)
    private Set<Moderator> moderators = new HashSet<>();
}
