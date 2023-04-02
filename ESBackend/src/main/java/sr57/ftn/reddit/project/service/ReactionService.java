package sr57.ftn.reddit.project.service;

import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Reaction;
import sr57.ftn.reddit.project.repository.ReactionRepository;

import java.util.List;

@Service
public class ReactionService {
    final private ReactionRepository reactionRepository;

    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public Reaction findOne(Integer id) {
        return reactionRepository.findById(id).orElseGet(null);
    }

    public List<Reaction> findAll() {
        return reactionRepository.findAll();
    }

    public void remove(Integer id) {
        reactionRepository.deleteById(id);
    }

    public void deleteByPostId(Integer post_id) {
        reactionRepository.deleteByPostId(post_id);
    }

    public void deleteCommentReactionsByPostId(Integer post_Id) {
        reactionRepository.deleteCommentReactionsByPostId(post_Id);
    }

    public void deleteByCommentId(Integer comment_id) {
        reactionRepository.deleteByCommentId(comment_id);
    }

    public Reaction save(Reaction reaction) {
        return reactionRepository.save(reaction);
    }
}
