package sr57.ftn.reddit.project.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rule")
public class Rule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id", unique = true, nullable = false)
    private Integer rule_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    //Rule has one Community
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;
}
