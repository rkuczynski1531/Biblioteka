package projekt.biblioteka.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    void deleteBookById(Integer id);

    @EntityGraph(attributePaths={"profilePicture"})
    Book findWithPropertyPictureAttachedById(Integer id);
}
