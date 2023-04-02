package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Community;
import sr57.ftn.reddit.project.model.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Integer> {
    Optional<Community> findFirstByName(String name);

    @Query(nativeQuery = true, value = "select * from Community community where community.is_suspended = false")
    List<Community> findAllNonSuspended();
}
