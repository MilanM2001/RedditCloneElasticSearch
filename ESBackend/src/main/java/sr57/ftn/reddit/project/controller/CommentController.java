package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticComment;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;
import sr57.ftn.reddit.project.elasticservice.ElasticPostService;
import sr57.ftn.reddit.project.model.dto.commentDTOs.AddCommentDTO;
import sr57.ftn.reddit.project.model.dto.commentDTOs.CommentDTO;
import sr57.ftn.reddit.project.model.dto.commentDTOs.UpdateCommentDTO;
import sr57.ftn.reddit.project.model.dto.reportDTOs.AddReportDTO;
import sr57.ftn.reddit.project.model.entity.*;
import sr57.ftn.reddit.project.model.enums.ReactionType;
import sr57.ftn.reddit.project.model.enums.ReportStatus;
import sr57.ftn.reddit.project.service.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    final CommentService commentService;
    final ModelMapper modelMapper;
    final UserService userService;
    final ReactionService reactionService;
    final ReportService reportService;
    final PostService postService;
    final ElasticPostService elasticPostService;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper,
                             UserService userService, ReactionService reactionService,
                             ReportService reportService, PostService postService,
                             ElasticPostService elasticPostService) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.reactionService = reactionService;
        this.reportService = reportService;
        this.postService = postService;
        this.elasticPostService = elasticPostService;
    }

    @GetMapping(value = "/single/{comment_id}")
    public ResponseEntity<CommentDTO> GetComment(@PathVariable("comment_id") Integer comment_id) {
        Comment comment = commentService.findOne(comment_id);
        return comment == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(comment, CommentDTO.class), HttpStatus.OK);
    }

    @PostMapping(value = "/add/{post_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddCommentDTO> AddComment(@RequestBody AddCommentDTO addCommentDTO, @PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);
        ElasticPost elasticPost = elasticPostService.findByPostId(post_id);

        Comment newComment = new Comment();

        newComment.setText(addCommentDTO.getText());
        newComment.setTimestamp(LocalDate.now());
        newComment.setIs_deleted(false);
        newComment.setUser(user);
        newComment.setPost(post);

        newComment = commentService.save(newComment);

        Reaction newReaction = new Reaction();

        newReaction.setUser(user);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setComment(newComment);
        newReaction.setReaction_type(ReactionType.UPVOTE);
        newReaction.setPost(null);

        ElasticComment elasticComment = new ElasticComment();
        elasticComment.setText(addCommentDTO.getText());

        Integer numberOfComments = commentService.countCommentsByPostId(post_id);

        elasticPost.setNumberOfComments(numberOfComments);
        Set<ElasticComment> postComments = elasticPost.getComments();
        postComments.add(elasticComment);
        elasticPost.setComments(postComments);
        elasticPostService.update(elasticPost);

        reactionService.save(newReaction);
        return new ResponseEntity<>(modelMapper.map(newComment, AddCommentDTO.class), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/addReport/{comment_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddReportDTO> ReportComment(@RequestBody AddReportDTO addReportDTO, @PathVariable("comment_id") Integer comment_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Comment comment = commentService.findOne(comment_id);

        Report newReport = new Report();

        newReport.setReport_reason(addReportDTO.getReport_reason());
        newReport.setReport_status(ReportStatus.WAITING);
        newReport.setTimestamp(LocalDate.now());
        newReport.setAccepted(false);
        newReport.setComment(comment);
        newReport.setPost(null);
        newReport.setUser(user);

        newReport = reportService.save(newReport);

        return new ResponseEntity<>(modelMapper.map(newReport, AddReportDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{comment_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateCommentDTO> UpdateComment(@RequestBody UpdateCommentDTO updateCommentDTO, @PathVariable("comment_id") Integer comment_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Comment comment = commentService.findOne(comment_id);

        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (user.getUser_id() != comment.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        comment.setText(updateCommentDTO.getText());

        comment = commentService.save(comment);
        return new ResponseEntity<>(modelMapper.map(comment, UpdateCommentDTO.class), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{comment_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<Void> DeleteComment(@PathVariable("comment_id") Integer comment_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Comment comment = commentService.findOne(comment_id);

        if (user.getUser_id() != comment.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        reactionService.deleteByCommentId(comment_id);
        reportService.deleteByCommentId(comment_id);
        commentService.remove(comment_id);

        ElasticPost elasticPost = elasticPostService.findByPostId(comment.getPost().getPost_id());
        Integer numberOfComments = commentService.countCommentsByPostId(comment.getPost().getPost_id());

        elasticPost.setNumberOfComments(numberOfComments);
        elasticPostService.update(elasticPost);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
