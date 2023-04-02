package sr57.ftn.reddit.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Post;
import sr57.ftn.reddit.project.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    final PostRepository postRepository;
    final UserService userService;
    final CommunityService communityService;
    final FlairService flairService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, CommunityService communityService, FlairService flairService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.communityService = communityService;
        this.flairService = flairService;
    }

    public Post findOne(Integer id) {
        return postRepository.findById(id).orElseGet(null);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void remove(Integer id) {
        postRepository.deleteById(id);
    }

    public List<Post> findAllFromNonSuspendedCommunity() {
        return postRepository.findAllFromNonSuspendedCommunity();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> findPostsByCommunityId(Integer community_id) {
        return postRepository.findPostsByCommunityId(community_id);
    }

    public List<Post> findPostsByUserId(Integer user_id) {
        return postRepository.findPostsByUserId(user_id);
    }
}
