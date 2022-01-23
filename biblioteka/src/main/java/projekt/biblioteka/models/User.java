package projekt.biblioteka.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import projekt.biblioteka.services.UsersBooksService;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Imię nie może być puste")
    @Pattern(regexp = "[^0-9@!#$%^&*()_,<.>/?=+\\\\{};:|\"\\[\\]]*", message = "Wprowadzono niepoprawne znaki")
    private String firstName;
    @NotBlank(message = "Nazwisko nie może być puste")
    @Pattern(regexp = "[^0-9@!#$%^&*()_,<.>/?=+\\\\{};:|\"\\[\\]]*", message = "Wprowadzono niepoprawne znaki")
    private String lastName;
    @NumberFormat
    @Pattern(regexp="[\\d]{9}", message = "Numer telefonu powinien zawierać tylko 9 cyfr")
    private String phoneNumber;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email nie może być pusty")
    @Email
    private String email;
    @NotBlank(message = "Hasło nie może być puste")
    private String password;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();
//    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
//    @JoinTable(
//            name = "users_books",
//            joinColumns = @JoinColumn(
//                    name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(
//                    name = "book_id", referencedColumnName = "id"))
//    private Set<Book> books = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<UsersBooks> books = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Comment> comments = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiverId")
    private Set<Message> messagesReceived = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "senderId")
    private Set<Message> messagesSent = new HashSet<>();
    @ManyToMany(mappedBy = "peopleInQueue")
    private List<Book> queueForBooks = new ArrayList<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }
    public void deleteRole(Role role) {
        this.roles.remove(role);
    }

    public void addBook(UsersBooks usersBooks) {
//        for(UsersBooks usersBook : usersBooks) usersBook.setUser(this);
//        this.books = Stream.of(usersBooks).collect(Collectors.toSet());
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }
}
