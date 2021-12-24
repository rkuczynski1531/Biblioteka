package projekt.biblioteka.services;

import org.springframework.stereotype.Service;
import projekt.biblioteka.models.Comment;
import projekt.biblioteka.repositories.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void saveComment(Comment comment){
        commentRepository.save(comment);
    }
    public void deleteCommentById(int id){
        commentRepository.deleteById(id);
    }

    public Comment findById(int id){
        return commentRepository.findById(id).orElse(null);
    }
    public List<Comment> getAll(){
        return commentRepository.findAll();
    }
}
