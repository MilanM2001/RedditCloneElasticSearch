package sr57.ftn.reddit.project.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flair")
public class Flair implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flair_id", unique = true, nullable = false)
    private Integer flair_id;

    @Column(name = "name", nullable = false)
    private String name;

    //Flair has one Community
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

//    @OneToMany(mappedBy = "flair", fetch = FetchType.EAGER)
//    private Set<Post> posts = new HashSet<Post>();
}
