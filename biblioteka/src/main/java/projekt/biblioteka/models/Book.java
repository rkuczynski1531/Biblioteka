package projekt.biblioteka.models;

//import org.springframework.content.commons.annotations.ContentId;
//import org.springframework.content.commons.annotations.ContentLength;
//import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "author_id", referencedColumnName = "id"))
    private List<Author> authors = new ArrayList<>();
    @NotBlank(message = "Opis nie może być pusty")
    @Column(length = 3000)
    private String description;
    @NotBlank(message = "Kategoria nie może być pusta")
    private String category;
    @NotNull(message = "Liczba stron nie może być pusta")
    @Min(message = "Liczba stron musi być większa od 0", value = 1)
    private int numOfPages;
//    @ManyToMany(mappedBy = "books")
//    private Set<User> users;
    @OneToMany(mappedBy = "book")
    private Set<UsersBooks> users = new HashSet<>();
    @OneToMany(mappedBy = "book")
    private List<Comment> comments = new ArrayList<>();
    private double rating;
    private int numOfVotes;

    private int quantity;
    private boolean onTheList = true;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "image_id", referencedColumnName = "id")

    private String image;
    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(
            name = "books_queue",
            joinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private List<User> peopleInQueue;


    public Book(String tytul, String opis, int iloscStron, String kategoria) {
        this.title = tytul;
        this.description = opis;
        this.numOfPages = iloscStron;
        this.category = kategoria;
        this.rating = 0;
        this.numOfVotes = 0;
    }

    public Book() {

    }

    public boolean isOnTheList() {
        return onTheList;
    }

    public void setOnTheList(boolean onTheList) {
        this.onTheList = onTheList;
    }

    public List<User> getPeopleInQueue() {
        return peopleInQueue;
    }

    public void setPeopleInQueue(List<User> peopleInQueue) {
        this.peopleInQueue = peopleInQueue;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String opis) {
        this.description = opis;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int iloscStron) {
        this.numOfPages = iloscStron;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double ocena) {
        this.rating = ocena;
    }

    public int getNumOfVotes() {
        return numOfVotes;
    }

    public void setNumOfVotes(int iloscGlosow) {
        this.numOfVotes = iloscGlosow;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<UsersBooks> getUsers() {
        return users;
    }

    public void setUsers(Set<UsersBooks> users) {
        this.users = users;
    }

    @Transient
    public String getPhotosImagePath() {
        if (image == null)
            return null;

        return "/book-photos/" + image;
    }

//    @ContentId
//    private UUID contentId;
//
//    @ContentLength
//    private Long contentLen;
//
//    @MimeType
//    private String mimeType;
}
