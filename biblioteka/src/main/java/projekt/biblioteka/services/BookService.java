package projekt.biblioteka.services;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.models.Category;
import projekt.biblioteka.repositories.BookRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
//        toSave.setImage(fileName);
        repository.save(toSave);
    }

    public void deleteBook(int id){
        repository.deleteBookById(id);
    }

//    public void saveImage(MultipartFile multipartFile, String fileName, int bookId) throws IOException {
//        String uploadDir = "book-images/" + bookId;
//
//        Path uploadPath = Paths.get(uploadDir);
//
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        try (InputStream inputStream = multipartFile.getInputStream()) {
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException ioe) {
//            throw new IOException("Could not save image file: " + fileName, ioe);
//        }
//    }
    public void saveImage(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
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
    public List<Book> findByKeyword(String keyword){
        return repository.findBookByTitleContainingIgnoreCase(keyword);
    }

    public Page<Book> findPaginated(int pageNo, int pageSize, String sortField, String sortDir, String keyword, boolean onTheList, List<Category> category){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        if (keyword != null){
            if (category.size() > 0) {
                Page<Book> toReturn;
                List<Book> list = new ArrayList<>();
                List<Book> list2 = new ArrayList<>();
                for (Category cat : category) {
                    toReturn = repository.findAllByOnTheListEqualsAndCategoriesContaining(keyword, onTheList, cat, pageable);
                    list = toReturn.toList();
                    for (Book b : list){
                        list2.add(b);
                    }
                }
                Page<Book> page = new PageImpl<>(list2);
                return page;
            }
            else
                return repository.findAllByOnTheListEquals(keyword, onTheList, pageable);
        }
        if (category.size() > 0) {
            Page<Book> toReturn;
            List<Book> list = new ArrayList<>();
            List<Book> list2 = new ArrayList<>();
            for (Category cat : category) {
                toReturn = repository.findAllByOnTheListEqualsAndCategoriesContaining(onTheList, cat, pageable);
                list = toReturn.toList();
                for (Book b : list){
                    list2.add(b);
                }
            }
            Page<Book> page = new PageImpl<>(list2);
            return page;
//            return this.repository.findAllByOnTheListEqualsAndCategoriesContaining(onTheList, category, pageable);
        }
        return this.repository.findAllByOnTheListEquals(onTheList, pageable);
    }

    public void deleteBookFromList(Book book){
        book.setOnTheList(false);
    }
    public void deleteBookFromListById(int id){
        this.getBook(id).setOnTheList(false);
    }
}
