package sr57.ftn.reddit.project.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer user_id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "avatar", nullable = false)
    private String avatar;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registration_date;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "display_name", nullable = false)
    private String display_name;

    //User has many Posts
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Post> posts = new HashSet<>();

    //User has many Comments
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    //User has many Bans
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Banned> bans = new HashSet<>();

    //User has many Reactions
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Reaction> reactions = new HashSet<>();

    //User can Moderate more Communities
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Moderator> moderators = new HashSet<>();

    public String getRole() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        if (user.username == null || username == null) {
            return false;
        }
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
