package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Moderator;

import javax.transaction.Transactional;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Moderator moderator where moderator.community.community_id = ?1")
    void deleteByCommunityId(Integer community_id);

}
