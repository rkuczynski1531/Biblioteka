package projekt.biblioteka.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String description;
    @NotBlank(message = "Kategoria nie może być pusta")
    private String category;
    @NotNull(message = "Liczba stron nie może być pusta")
    @Min(message = "Liczba stron musi być większa od 0", value = 1)
    private int numOfPages;
    private double rating;
    private int numOfVotes;
    private int quantity;
    private String image;

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

    @Transient
    public String getPhotosImagePath() {
        if (image == null)
            return null;

        return "/book-images/" + id + "/" + image;
    }
}
