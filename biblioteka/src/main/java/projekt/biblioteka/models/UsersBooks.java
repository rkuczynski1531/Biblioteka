package projekt.biblioteka.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class UsersBooks {
//    @EmbeddedId
//    private UsersBooksId id = new UsersBooksId();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
//    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @ManyToOne()
//    @MapsId("bookId")
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    private boolean returned;

    private LocalDateTime deadlineForReturn;
    private LocalDateTime dateOfReturn;
    private double rating;
    private boolean borrowed;


    public UsersBooks(User user) {
        this.user = user;
    }

    public UsersBooks(Book book) {
        this.book = book;
    }

    public UsersBooks(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersBooks that = (UsersBooks) o;
        return returned == that.returned && user.equals(that.user) && book.equals(that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, book, returned);
    }
}
