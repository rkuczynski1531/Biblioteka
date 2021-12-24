package projekt.biblioteka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
