package sr57.ftn.reddit.project.service;

import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Report;
import sr57.ftn.reddit.project.repository.ReportRepository;

import java.util.List;

@Service
public class ReportService {
    final private ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report findOne(Integer id) {
        return reportRepository.findById(id).orElseGet(null);
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public List<Report> findAllReportedPost() {
        return reportRepository.findAllReportedPost();
    }

    public List<Report> findAllReportedComment() {
        return reportRepository.findAllReportedComment();
    }

    public void remove(Integer id) {
        reportRepository.deleteById(id);
    }

    public void deleteByPostId(Integer post_id) {
        reportRepository.deleteByPostId(post_id);
    }

    public void deleteByCommentId(Integer comment_id) {
        reportRepository.deleteByCommentId(comment_id);
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }

}
