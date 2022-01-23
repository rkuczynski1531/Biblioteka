package projekt.biblioteka.models;

//import org.springframework.content.commons.annotations.ContentId;
//import org.springframework.content.commons.annotations.ContentLength;
//import org.springframework.content.commons.annotations.MimeType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private List<Category> categories = new ArrayList<>();
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



    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    @Transient
    public String getPhotosImagePath() {
        if (image == null)
            return null;

        return "/book-photos/" + image;
    }

}
