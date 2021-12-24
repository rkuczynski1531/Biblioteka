package projekt.biblioteka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.models.User;
import projekt.biblioteka.models.UsersBooks;

import java.util.List;

@Repository
public interface UsersBooksRepository extends JpaRepository<UsersBooks, Integer> {
    public UsersBooks findUsersBooksByUserEqualsAndBookEquals(User user, Book book);
    public boolean existsByBookEqualsAndUserEquals(Book book, User user);
    public boolean existsByBookEqualsAndUserEqualsAndBorrowedEquals(Book book, User user, boolean borrowed);
//    public UsersBooks findUsersBooksByBookEqualsAndUserEquals(Book book, User user);
    public List<UsersBooks> findUsersBooksByUserEqualsAndBorrowedEquals(User user, boolean borrowed);
    public List<UsersBooks> findUsersBooksByUserEqualsAndReturnedEquals(User user, boolean borrowed);
    public UsersBooks findUsersBooksByUserEqualsAndBookEqualsAndBorrowedEquals(User user, Book book, boolean borrowed);
    public List<UsersBooks> findUsersBooksByUserEqualsAndBookEqualsAndReturnedEquals(User user, Book book, boolean borrowed);
    public List<UsersBooks> findUsersBooksByBookEqualsAndBorrowedEquals(Book book, boolean borrowed);
    public List<UsersBooks> findUsersBooksByBookEqualsAndUserEqualsAndRatingGreaterThan(Book book, User user, double rating);
    public List<UsersBooks> findUsersBooksByBookEqualsAndUserEquals(Book book, User user);
}
