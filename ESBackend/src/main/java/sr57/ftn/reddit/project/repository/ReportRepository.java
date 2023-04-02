package sr57.ftn.reddit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sr57.ftn.reddit.project.model.entity.Report;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    @Query(nativeQuery = true, value = "select * from report where post_id not like 'null' and report_status like 'WAITING'")
    List<Report> findAllReportedPost();

    @Query(nativeQuery = true, value = "select * from report where comment_id not like 'null' and report_status like 'WAITING'")
    List<Report> findAllReportedComment();

    @Transactional
    @Modifying
    @Query("delete from Report report where report.post.post_id = ?1")
    void deleteByPostId(Integer post_id);

    @Transactional
    @Modifying
    @Query("delete from Report report where report.comment.comment_id = ?1")
    void deleteByCommentId(Integer comment_id);
}
