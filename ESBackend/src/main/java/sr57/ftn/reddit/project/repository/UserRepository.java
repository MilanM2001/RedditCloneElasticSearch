package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByEmail(String email);

    @Query("select user from User user join fetch user.posts post where user.user_id = ?1")
    User findOneWithPosts(Integer post_id);
}
