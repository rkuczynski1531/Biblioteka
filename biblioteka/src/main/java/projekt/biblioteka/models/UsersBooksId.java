package projekt.biblioteka.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsersBooksId implements Serializable {
    private static final long serialVersionUID = 1L;
    private int bookId;
    private int userId;

    public UsersBooksId() {
    }

    public UsersBooksId(int bookId, int userId) {
        super();
        this.bookId = bookId;
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersBooksId that = (UsersBooksId) o;
        return bookId == that.bookId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, userId);
    }
}
