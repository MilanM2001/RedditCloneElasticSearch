package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Flair;

import java.util.List;

@Repository
public interface FlairRepository extends JpaRepository<Flair, Integer> {

    @Query(nativeQuery = true, value = "Select * From flair where flair.community_id = ?")
    List<Flair> findFlairsByCommunityId(Integer community_id);
}
