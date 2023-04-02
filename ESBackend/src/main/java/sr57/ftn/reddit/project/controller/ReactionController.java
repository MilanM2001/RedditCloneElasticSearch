package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.CommentReactionAndroidDTO;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.PostReactionAndroidDTO;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionDTO;
import sr57.ftn.reddit.project.model.dto.reactionDTOs.ReactionForCommentAndPost;
import sr57.ftn.reddit.project.model.entity.Reaction;
import sr57.ftn.reddit.project.model.entity.User;
import sr57.ftn.reddit.project.model.enums.ReactionType;
import sr57.ftn.reddit.project.service.CommentService;
import sr57.ftn.reddit.project.service.PostService;
import sr57.ftn.reddit.project.service.ReactionService;
import sr57.ftn.reddit.project.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "api/reactions")
public class ReactionController {
    final UserService userService;
    final ModelMapper modelMapper;
    final ReactionService reactionService;
    final PostService postService;
    final CommentService commentService;

    @Autowired
    public ReactionController(UserService userService, ModelMapper modelMapper, ReactionService reactionService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.reactionService = reactionService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<ReactionDTO>> getAll() {
        List<Reaction> reactions = reactionService.findAll();

        List<ReactionDTO> reactionsDTO = modelMapper.map(reactions, new TypeToken<List<ReactionDTO>>() {
        }.getType());
        return new ResponseEntity<>(reactionsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{reaction_id}")
    public ResponseEntity<ReactionDTO> getReaction(@PathVariable("reaction_id") Integer reaction_id) {
        Reaction reaction = reactionService.findOne(reaction_id);
        return reaction == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(reaction, ReactionDTO.class), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<ReactionForCommentAndPost> create(@RequestBody ReactionDTO reactionDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

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

        return new ResponseEntity<>(modelMapper.map(savedReaction, ReactionForCommentAndPost.class), HttpStatus.CREATED);
    }


    //Android

    @PostMapping(value = "/upvotePostAndroid")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<PostReactionAndroidDTO> upvotePost(@RequestBody PostReactionAndroidDTO postReactionAndroidDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        Reaction newReaction = new Reaction();

        newReaction.setPost(postService.findOne(postReactionAndroidDTO.getPost_id()));

        newReaction.setUser(user);
        newReaction.setComment(null);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setReaction_type(ReactionType.UPVOTE);

        Reaction savedReaction = reactionService.save(newReaction);

        return new ResponseEntity<>(modelMapper.map(savedReaction, PostReactionAndroidDTO.class), HttpStatus.CREATED);
    }

    @PostMapping(value = "/downvotePostAndroid")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<PostReactionAndroidDTO> downvotePost(@RequestBody PostReactionAndroidDTO postReactionAndroidDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        Reaction newReaction = new Reaction();

        newReaction.setPost(postService.findOne(postReactionAndroidDTO.getPost_id()));

        newReaction.setUser(user);
        newReaction.setComment(null);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setReaction_type(ReactionType.DOWNVOTE);

        Reaction savedReaction = reactionService.save(newReaction);

        return new ResponseEntity<>(modelMapper.map(savedReaction, PostReactionAndroidDTO.class), HttpStatus.CREATED);
    }

    @PostMapping(value = "/upvoteCommentAndroid")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<CommentReactionAndroidDTO> upvoteComment(@RequestBody CommentReactionAndroidDTO commentReactionAndroidDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        Reaction newReaction = new Reaction();

        newReaction.setComment(commentService.findOne(commentReactionAndroidDTO.getComment_id()));

        newReaction.setUser(user);
        newReaction.setPost(null);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setReaction_type(ReactionType.UPVOTE);

        Reaction savedReaction = reactionService.save(newReaction);

        return new ResponseEntity<>(modelMapper.map(savedReaction, CommentReactionAndroidDTO.class), HttpStatus.CREATED);
    }

    @PostMapping(value = "/downvoteCommentAndroid")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<CommentReactionAndroidDTO> downvoteComment(@RequestBody CommentReactionAndroidDTO commentReactionAndroidDTO, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());

        Reaction newReaction = new Reaction();

        newReaction.setComment(commentService.findOne(commentReactionAndroidDTO.getComment_id()));

        newReaction.setUser(user);
        newReaction.setPost(null);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setReaction_type(ReactionType.UPVOTE);

        Reaction savedReaction = reactionService.save(newReaction);

        return new ResponseEntity<>(modelMapper.map(savedReaction, CommentReactionAndroidDTO.class), HttpStatus.CREATED);
    }

}
