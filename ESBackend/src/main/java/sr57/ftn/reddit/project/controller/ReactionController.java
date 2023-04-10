package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;
import sr57.ftn.reddit.project.elasticservice.ElasticPostService;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionDTO;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionForCommentAndPost;
import sr57.ftn.reddit.project.model.entity.Post;
import sr57.ftn.reddit.project.model.entity.Reaction;
import sr57.ftn.reddit.project.model.entity.User;
import sr57.ftn.reddit.project.model.enums.ReactionType;
import sr57.ftn.reddit.project.service.CommentService;
import sr57.ftn.reddit.project.service.PostService;
import sr57.ftn.reddit.project.service.ReactionService;
import sr57.ftn.reddit.project.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "api/reactions")
public class ReactionController {
    final UserService userService;
    final ModelMapper modelMapper;
    final ReactionService reactionService;
    final PostService postService;
    final CommentService commentService;
    final ElasticPostService elasticPostService;

    @Autowired
    public ReactionController(UserService userService, ModelMapper modelMapper, ReactionService reactionService, PostService postService, CommentService commentService, ElasticPostService elasticPostService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.reactionService = reactionService;
        this.postService = postService;
        this.commentService = commentService;
        this.elasticPostService = elasticPostService;
    }

    @GetMapping(value = "/postKarma/{post_id}")
    public Integer calculateKarmaForPost(@PathVariable("post_id") Integer post_id) {
        Post post = postService.findOne(post_id);
        Set<Reaction> postReactions = post.getReactions();

        Integer karma = 0;

        for(Reaction reaction : postReactions) {
            if (reaction.getReaction_type().equals(ReactionType.UPVOTE)) {
                karma++;
            } else if (reaction.getReaction_type().equals(ReactionType.DOWNVOTE)) {
                karma--;
            }
        }
        return karma;
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<ReactionForCommentAndPost> create(@RequestBody ReactionDTO reactionDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        Reaction existingReaction = reactionService.findByPostAndUser(reactionDTO.getPost_id(), user.getUser_id());

        if (existingReaction != null) {
            reactionService.deleteByPostAndUser(reactionDTO.getPost_id(), user.getUser_id());
        }

        Reaction newReaction = new Reaction();

        if (reactionDTO.getPost_id() != 0) {
            newReaction.setPost(postService.findOne(reactionDTO.getPost_id()));
        }

        if (reactionDTO.getComment_id() != 0) {
            newReaction.setComment(commentService.findOne(reactionDTO.getComment_id()));
        }

        newReaction.setUser(user);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setReaction_type(reactionDTO.getReaction_type());

        Reaction savedReaction = reactionService.save(newReaction);

        Post post = postService.findOne(reactionDTO.getPost_id());
        Set<Reaction> postReactions = post.getReactions();

        int karma = 0;

        for(Reaction reaction : postReactions) {
            if (reaction.getReaction_type().equals(ReactionType.UPVOTE)) {
                karma++;
            } else if (reaction.getReaction_type().equals(ReactionType.DOWNVOTE)) {
                karma--;
            }
        }

        ElasticPost elasticPost = elasticPostService.findByPostId(reactionDTO.getPost_id());
        elasticPost.setKarma(karma);
        elasticPostService.update(elasticPost);

        return new ResponseEntity<>(modelMapper.map(savedReaction, ReactionForCommentAndPost.class), HttpStatus.CREATED);
    }
}
