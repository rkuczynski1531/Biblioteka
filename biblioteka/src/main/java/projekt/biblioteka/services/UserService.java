package projekt.biblioteka.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.models.User;
import projekt.biblioteka.models.UsersBooks;
import projekt.biblioteka.repositories.UserRepository;
import projekt.biblioteka.repositories.UsersBooksRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UsersBooksRepository usersBooksRepository;

    public UserService(UserRepository userRepository, UsersBooksRepository usersBooksRepository) {
        this.userRepository = userRepository;
        this.usersBooksRepository = usersBooksRepository;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void borrowBook(Book book){

    }

    public void saveUser(User toSave){
        toSave.getBooks().stream()
                .forEach(it -> {
                    it.setUser(toSave);
                });
        userRepository.save(toSave);
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return getUserByEmail(currentPrincipalName);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public User getUserById(int id){
        return userRepository.findById(id).orElse(null);
    }

    public Page<User> findPaginated(int pageNo, int pageSize, String keyword){
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        if (keyword != null){
            return userRepository.findAll(keyword, pageable);
        }
        return this.userRepository.findAll(pageable);
    }

    public List<UsersBooks> getBorrowedBooks(User user){
        return usersBooksRepository.findUsersBooksByUserEqualsAndBorrowedEquals(user, true);
    }

    public List<UsersBooks> getReturnedBooks(User user){
        return usersBooksRepository.findUsersBooksByUserEqualsAndReturnedEquals(user, true);
    }

//    public List<UsersBooks> getRatedBooks(Book book, User user){
//        return usersBooksRepository.findUsersBooksByBookEqualsAndUserEqualsAndRatingExists(book, user);
//    }
}
