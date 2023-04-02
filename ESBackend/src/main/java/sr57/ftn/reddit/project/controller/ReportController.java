package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.reportDTOs.SimpleInfoReportDTO;
import sr57.ftn.reddit.project.model.dto.reportDTOs.UpdateReportDTO;
import sr57.ftn.reddit.project.model.entity.Report;
import sr57.ftn.reddit.project.model.enums.ReportStatus;
import sr57.ftn.reddit.project.service.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/reports")
public class ReportController {
    final UserService userService;
    final ModelMapper modelMapper;
    final ReportService reportService;
    final PostService postService;
    final CommentService commentService;
    final ReactionService reactionService;

    @Autowired
    public ReportController(UserService userService, ModelMapper modelMapper, ReportService reportService, PostService postService, CommentService commentService, ReactionService reactionService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.reportService = reportService;
        this.postService = postService;
        this.commentService = commentService;
        this.reactionService = reactionService;
    }

    @GetMapping(value = "/allReportedPost")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CrossOrigin
    public ResponseEntity<List<SimpleInfoReportDTO>> getAllReportedPost() {
        List<Report> reports = reportService.findAllReportedPost();

        List<SimpleInfoReportDTO> reportsDTO = modelMapper.map(reports, new TypeToken<List<SimpleInfoReportDTO>>() {
        }.getType());
        return new ResponseEntity<>(reportsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/allReportedComment")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CrossOrigin
    public ResponseEntity<List<SimpleInfoReportDTO>> getAllReportedComment() {
        List<Report> reports = reportService.findAllReportedComment();

        List<SimpleInfoReportDTO> reportsDTO = modelMapper.map(reports, new TypeToken<List<SimpleInfoReportDTO>>() {
        }.getType());
        return new ResponseEntity<>(reportsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{report_id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CrossOrigin
    public ResponseEntity<SimpleInfoReportDTO> getReport(@PathVariable("report_id") Integer report_id) {
        Report report = reportService.findOne(report_id);
        return report == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(report, SimpleInfoReportDTO.class), HttpStatus.OK);
    }

    @PutMapping(value = "/updateReport/{report_id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateReportDTO> updateReport(@RequestBody UpdateReportDTO updateReportDTO, @PathVariable("report_id") Integer report_id) {
        Report report = reportService.findOne(report_id);

        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        report.setReport_status(updateReportDTO.getReport_status());

        if (updateReportDTO.getReport_status().equals(ReportStatus.ACCEPTED)) {
            report.setAccepted(true);
        }

        if (updateReportDTO.getReport_status().equals(ReportStatus.DENIED)) {
            report.setAccepted(false);
        }

        report = reportService.save(report);
        return new ResponseEntity<>(modelMapper.map(report, UpdateReportDTO.class), HttpStatus.OK);
    }
}
