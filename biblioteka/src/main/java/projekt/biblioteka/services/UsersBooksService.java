package projekt.biblioteka.services;

import org.springframework.stereotype.Service;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.models.User;
import projekt.biblioteka.models.UsersBooks;
import projekt.biblioteka.repositories.UsersBooksRepository;

import java.util.List;

@Service
public class UsersBooksService {
    private final UsersBooksRepository usersBooksRepository;

    public UsersBooksService(UsersBooksRepository usersBooksRepository) {
        this.usersBooksRepository = usersBooksRepository;
    }

    public List<UsersBooks> getAllUsersBooks(){
        return usersBooksRepository.findAll();
    }

    public UsersBooks getUsersBooksById(int id){
        return usersBooksRepository.findById(id).orElse(null);
    }

    public UsersBooks getUsersBooksByUserAndBook(User user, Book book){
        return usersBooksRepository.findUsersBooksByUserEqualsAndBookEquals(user, book);
    }

    public List<UsersBooks> getUsersBooksByBookAndBorrowed(Book book, boolean borrowed){
        return usersBooksRepository.findUsersBooksByBookEqualsAndBorrowedEquals(book, borrowed);
    }

    public List<UsersBooks> getRatedBooks(Book book, User user){
        return usersBooksRepository.findUsersBooksByBookEqualsAndUserEqualsAndRatingGreaterThan(book, user, 0);
    }

    public List<UsersBooks> getListUsersBooks(Book book, User user){
        return usersBooksRepository.findUsersBooksByBookEqualsAndUserEquals(book, user);
    }

    public UsersBooks getUsersBooksByUserAndBookAndBorrowed(User user, Book book){
        return usersBooksRepository.findUsersBooksByUserEqualsAndBookEqualsAndBorrowedEquals(user, book, true);
    }

//    public List<UsersBooks> getReturned

    public void saveUsersBooks(UsersBooks usersBooks){
        usersBooksRepository.save(usersBooks);
    }
    public boolean existsByUserAndBook(User user, Book book){
        return usersBooksRepository.existsByBookEqualsAndUserEquals(book, user);
    }

    public boolean existsByUserAndBookAndBorrowed(User user, Book book){
        return usersBooksRepository.existsByBookEqualsAndUserEqualsAndBorrowedEquals(book, user, true);
    }
    public UsersBooks getUsersBooksByBookAndUser(Book book, User user){
        return usersBooksRepository.findUsersBooksByUserEqualsAndBookEquals(user, book);
    }

}
