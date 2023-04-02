package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(nativeQuery = true, value = "Select * From post Where post.community_id = ?")
    List<Post> findPostsByCommunityId(Integer community_id);

    @Query(nativeQuery = true, value = "Select * From post Where post.user_id = ?")
    List<Post> findPostsByUserId(Integer user_id);

    @Query(nativeQuery = true, value = "Select * From post Left Join community On post.community_id = community.community_id Left Join report On post.post_id = report.post_id Where community.is_suspended = False And report.accepted Is Null Or report.accepted is False Order By rand()")
    List<Post> findAllFromNonSuspendedCommunity();
}
