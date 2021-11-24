package projekt.biblioteka.controllers;

import org.hibernate.TypeMismatchException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import projekt.biblioteka.models.Author;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.services.BookService;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
//@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/booksList")
    public String showBooks(Model model){
//        model.addAttribute("book", new Book());
        return "books/booksList";
    }

    @ModelAttribute("books")
    public List<Book> getBooks(){
        return bookService.getBooks();
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable int id, Model model){
        model.addAttribute("book", bookService.getBook(id));
        return "books/bookDetails";
    }
    @GetMapping("/addBook")
    public String addBookForm(Model model){
        model.addAttribute("book", new Book());
        return "books/addBook";
    }

//    @ExceptionHandler(TypeMismatchException.class)
//    @ResponseBody
//    public String handleTypeMismatchException(TypeMismatchException typeMismatchException) {
//        return null;
//    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute("book") @Valid Book toSave, BindingResult bindingResult,
                          @RequestParam("image") MultipartFile multipartFile,
                          RedirectAttributes redirectAttributes) throws IOException {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        toSave.setImage(fileName);

        bookService.saveBook(toSave);

        bookService.saveImage(multipartFile, fileName, toSave.getId());

        return "redirect:/booksList";
    }

    @PostMapping(value = "/addBook", params = "addAuthor")
    public String addAuthor(@ModelAttribute("book") Book current) {
        current.getAuthors().add(new Author());
        return "books/addBook";
    }

    @Transactional
    @PostMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable int id){
        bookService.deleteBook(id);
        return "redirect:/booksList";
    }

    @GetMapping("/updateBook/{id}")
    public String updateBookForm(@PathVariable int id, Model model){
        model.addAttribute("bookToUpdate", bookService.getBook(id));
        return "books/updateBook";
    }

    @PostMapping("/updateBook/{id}")
    public String updateBook(@ModelAttribute("bookToUpdate") @Valid Book toSave, BindingResult bindingResult,@RequestParam("image") MultipartFile multipartFile) throws IOException {
//        if (bindingResult.hasErrors()){
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors ) {
//                System.out.println (error.getObjectName() + " - " + error.getDefaultMessage());
//            }
//            return "books/updateBook";
//        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        toSave.setImage(fileName);

        bookService.saveBook(toSave);

        bookService.saveImage(multipartFile, fileName, toSave.getId());
        return "redirect:/booksList";
    }
}
