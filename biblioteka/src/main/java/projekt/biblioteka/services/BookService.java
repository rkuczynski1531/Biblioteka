package projekt.biblioteka.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.repositories.BookRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getBooks(){
        return repository.findAll();
    }

    public Book getBook(int id){
        return repository.findById(id).orElse(null);
    }

    public void saveBook(Book toSave){
        repository.save(toSave);
    }

    public void saveBook(Book toSave, String fileName){
        toSave.setImage(fileName);
        repository.save(toSave);
    }

    public void deleteBook(int id){
        repository.deleteBookById(id);
    }

    public void saveImage(MultipartFile multipartFile, String fileName, int bookId) throws IOException {
        String uploadDir = "book-images/" + bookId;

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
}
