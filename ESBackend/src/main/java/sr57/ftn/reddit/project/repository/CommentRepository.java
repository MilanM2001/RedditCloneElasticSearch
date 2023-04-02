package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Comment;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Comment comment where comment.post.post_id = ?1")
    void deleteByPostId(Integer post_id);

    @Query(nativeQuery = true, value = "Select * From comment Where comment.post_id = ?")
    List<Comment> findCommentsByPostId(Integer post_id);

}
