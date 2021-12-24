package projekt.biblioteka.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public UsersBooks() {
    }

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

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean wasBorrowed) {
        this.borrowed = wasBorrowed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

//    public UsersBooksId getId() {
//        return id;
//    }
//
//    public void setId(UsersBooksId id) {
//        this.id = id;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDeadlineForReturn() {
        return deadlineForReturn;
    }

    public void setDeadlineForReturn(LocalDateTime deadlineForReturn) {
        this.deadlineForReturn = deadlineForReturn;
    }

    public LocalDateTime getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(LocalDateTime dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
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
