package projekt.biblioteka.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    void deleteBookById(Integer id);

    @EntityGraph(attributePaths={"profilePicture"})
    Book findWithPropertyPictureAttachedById(Integer id);

    List<Book> findBookByTitleContainingIgnoreCase(String keyword);

    @Query("SELECT b FROM Book b WHERE lower(b.title) LIKE lower(concat('%', ?1,'%')) OR lower(b.category) LIKE lower(?1) AND b.onTheList = ?2")
    public Page<Book> findAllByOnTheListEquals(String keyword, boolean onTheList, Pageable page);


    public Page<Book> findAllByOnTheListEquals(Pageable page, boolean onTheList);
//    List<Book>
}
