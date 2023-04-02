package sr57.ftn.reddit.project.service;

import org.springframework.stereotype.Service;
import sr57.ftn.reddit.project.model.entity.Comment;
import sr57.ftn.reddit.project.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    final private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment findOne(Integer id) {
        return commentRepository.findById(id).orElseGet(null);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findCommentsByPostId(Integer post_id) {
        return commentRepository.findCommentsByPostId(post_id);
    }

    public void remove(Integer id) {
        commentRepository.deleteById(id);
    }

    public void deleteByPostId(Integer post_id) {
        commentRepository.deleteByPostId(post_id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
