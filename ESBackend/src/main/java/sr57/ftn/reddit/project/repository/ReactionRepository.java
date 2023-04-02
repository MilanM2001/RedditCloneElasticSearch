package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Reaction;

import javax.transaction.Transactional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Reaction reaction where reaction.post.post_id = ?1")
    void deleteByPostId(Integer post_id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE reaction from reaction LEFT JOIN comment on reaction.comment_id = comment.comment_id where comment.post_id = ?1")
    void deleteCommentReactionsByPostId(Integer post_id);

    @Transactional
    @Modifying
    @Query("delete from Reaction reaction where reaction.comment.comment_id = ?1")
    void deleteByCommentId(Integer comment_id);
}
