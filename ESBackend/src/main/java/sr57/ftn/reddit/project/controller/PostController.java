package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.elasticmodel.elasticdto.elasticpostDTOs.ElasticPostResponseDTO;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticFlair;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticPost;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticUser;
import sr57.ftn.reddit.project.elasticservice.ElasticCommunityService;
import sr57.ftn.reddit.project.elasticservice.ElasticPostService;
import sr57.ftn.reddit.project.model.dto.commentDTOs.CommentDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.AddPostDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.PostDTO;
import sr57.ftn.reddit.project.model.dto.postDTOs.UpdatePostDTO;
import sr57.ftn.reddit.project.model.dto.reportDTOs.AddReportDTO;
import sr57.ftn.reddit.project.model.entity.*;
import sr57.ftn.reddit.project.model.enums.ReactionType;
import sr57.ftn.reddit.project.model.enums.ReportStatus;
import sr57.ftn.reddit.project.service.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {
    final PostService postService;
    final ModelMapper modelMapper;
    final UserService userService;
    final CommunityService communityService;
    final CommentService commentService;
    final ReactionService reactionService;
    final ReportService reportService;
    final FlairService flairService;
    final ElasticCommunityService elasticCommunityService;
    final ElasticPostService elasticPostService;

    @Autowired
    public PostController(PostService postService, UserService userService,
                          CommunityService communityService, ModelMapper modelMapper,
                          CommentService commentService, ReactionService reactionService,
                          ReportService reportService, FlairService flairService,
                          ElasticCommunityService elasticCommunityService, ElasticPostService elasticPostService) {
        this.postService = postService;
        this.userService = userService;
        this.communityService = communityService;
        this.modelMapper = modelMapper;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.reportService = reportService;
        this.flairService = flairService;
        this.elasticCommunityService = elasticCommunityService;
        this.elasticPostService = elasticPostService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<PostDTO>> GetAll() {
        List<Post> posts = postService.findAllFromNonSuspendedCommunity();
        List<PostDTO> postsDTO = modelMapper.map(posts, new TypeToken<List<PostDTO>>() {}.getType());

        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/allElastic")
    public ResponseEntity<List<ElasticPost>> GetAllElastic() {
        List<ElasticPost> elasticPosts = elasticPostService.findAll();

        return new ResponseEntity<>(elasticPosts, HttpStatus.OK);
    }

    @GetMapping(value = "/single/{post_id}")
    public ResponseEntity<PostDTO> GetSingle(@PathVariable("post_id") Integer post_id) {
        Post post = postService.findOne(post_id);
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/findAllByTitle/{title}")
    public ResponseEntity<List<ElasticPost>> GetAllByTitle(@PathVariable String title) {
        List<ElasticPost> elasticPosts = elasticPostService.findAllByTitle(title);

        return new ResponseEntity<>(elasticPosts, HttpStatus.OK);
    }

    @GetMapping(value = "/findAllByText/{text}")
    public ResponseEntity<List<ElasticPost>> GetAllByText(@PathVariable String text) {
        List<ElasticPost> elasticPosts = elasticPostService.findAllByText(text);

        return new ResponseEntity<>(elasticPosts, HttpStatus.OK);
    }

    @GetMapping(value = "findAllByFlairName/{name}")
    public ResponseEntity<List<ElasticPost>> GetAllByFlairName(@PathVariable String name) {
        List<ElasticPost> elasticPosts = elasticPostService.findAllByFlairName(name);

        return new ResponseEntity<>(elasticPosts, HttpStatus.OK);
    }

    @GetMapping("/karma/{from}/to/{to}")
    public List<ElasticPostResponseDTO> getByKarmaRange(@PathVariable Integer from, @PathVariable Integer to) {
        return elasticPostService.findByKarma(from, to);
    }

    @GetMapping(value = "/comments/{post_id}")
    public ResponseEntity<List<CommentDTO>> GetPostComments(@PathVariable("post_id") Integer post_id) {
        List<Comment> comments = commentService.findCommentsByPostId(post_id);
        List<CommentDTO> commentsDTO = modelMapper.map(comments, new TypeToken<List<CommentDTO>>() {}.getType());

        return new ResponseEntity<>(commentsDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/add/{community_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddPostDTO> AddPost(@RequestBody AddPostDTO addPostDTO, @PathVariable("community_id") Integer community_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        ElasticUser elasticUser = new ElasticUser();
        elasticUser.setUsername(user.getUsername());

        Community community = communityService.findOne(community_id);
        ElasticCommunity elasticCommunity = elasticCommunityService.findByCommunityId(community_id);

        Post newPost = new Post();

        newPost.setTitle(addPostDTO.getTitle());
        newPost.setText(addPostDTO.getText());
        newPost.setCreation_date(LocalDate.now());
        newPost.setImage_path("none");
        newPost.setUser(user);
        newPost.setCommunity(community);
        if (addPostDTO.getFlair_id() != 0) {
            newPost.setFlair(flairService.findOne(addPostDTO.getFlair_id()));
        }
        newPost = postService.save(newPost);

        Reaction newReaction = new Reaction();

        newReaction.setUser(user);
        newReaction.setTimestamp(LocalDate.now());
        newReaction.setComment(null);
        newReaction.setReaction_type(ReactionType.UPVOTE);
        newReaction.setPost(newPost);
        reactionService.save(newReaction);

        ElasticPost elasticPost = new ElasticPost();

        elasticPost.setPost_id(newPost.getPost_id());
        elasticPost.setTitle(addPostDTO.getTitle());
        elasticPost.setText(addPostDTO.getText());
        elasticPost.setKarma(1);
        elasticPost.setUser(elasticUser);
        elasticPost.setCommunity(elasticCommunity);
        if (addPostDTO.getFlair_id() != 0) {
            Flair foundFlair = flairService.findOne(addPostDTO.getFlair_id());
            ElasticFlair elasticFlair= new ElasticFlair();

            elasticFlair.setFlair_id(foundFlair.getFlair_id());
            elasticFlair.setName(foundFlair.getName());
            elasticPost.setFlair(elasticFlair);
        }
        elasticPostService.index(elasticPost);

        Integer numberOfPosts = postService.countPostsByCommunityId(community_id);

        elasticCommunity.setNumberOfPosts(numberOfPosts);
        elasticCommunityService.index(elasticCommunity);

        return new ResponseEntity<>(modelMapper.map(newPost, AddPostDTO.class), HttpStatus.CREATED);
    }

    @PostMapping(value = "/addReport/{post_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddReportDTO> ReportPost(@RequestBody AddReportDTO addReportDTO, @PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);

        Report newReport = new Report();

        newReport.setReport_reason(addReportDTO.getReport_reason());
        newReport.setReport_status(ReportStatus.WAITING);
        newReport.setTimestamp(LocalDate.now());
        newReport.setAccepted(false);
        newReport.setComment(null);
        newReport.setPost(post);
        newReport.setUser(user);

        newReport = reportService.save(newReport);

        return new ResponseEntity<>(modelMapper.map(newReport, AddReportDTO.class), HttpStatus.CREATED);
    }

    @PutMapping(value = "update/{post_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdatePostDTO> UpdatePost(@RequestBody UpdatePostDTO updatePostDTO, @PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);
        ElasticPost elasticPost = elasticPostService.findByPostId(post_id);

        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (user.getUser_id() != post.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        post.setTitle(updatePostDTO.getTitle());
        post.setText(updatePostDTO.getText());
        postService.save(post);

        elasticPost.setTitle(updatePostDTO.getTitle());
        elasticPost.setText(updatePostDTO.getText());
        elasticPostService.update(elasticPost);

        return new ResponseEntity<>(modelMapper.map(post, UpdatePostDTO.class), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{post_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<Void> DeletePost(@PathVariable("post_id") Integer post_id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Post post = postService.findOne(post_id);
        Integer community_id = post.getCommunity().getCommunity_id();
        ElasticCommunity elasticCommunity = elasticCommunityService.findByCommunityId(community_id);

        if (user.getUser_id() != post.getUser().getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        reactionService.deleteCommentReactionsByPostId(post_id);
        reactionService.deleteByPostId(post_id);
        commentService.deleteByPostId(post_id);
        reportService.deleteByPostId(post_id);
        postService.remove(post_id);
        elasticPostService.remove(post_id);

        Integer numberOfPosts = postService.countPostsByCommunityId(community_id);

        elasticCommunity.setNumberOfPosts(numberOfPosts);
        elasticCommunityService.index(elasticCommunity);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}