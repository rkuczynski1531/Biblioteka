package projekt.biblioteka.controllers;

//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import projekt.biblioteka.config.BASE64DecodedMultipartFile;
//import projekt.biblioteka.config.CustomMultipartFile;
import projekt.biblioteka.models.*;
import projekt.biblioteka.repositories.ImageRepository;
import projekt.biblioteka.services.BookService;
import projekt.biblioteka.services.CommentService;
import projekt.biblioteka.services.UserService;
import projekt.biblioteka.services.UsersBooksService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Controller
//@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final UserService userService;
    private final CommentService commentService;
    private final ImageRepository imageRepository;
    private final UsersBooksService usersBooksService;

    public BookController(BookService bookService, UserService userService, CommentService commentService, ImageRepository imageRepository, UsersBooksService usersBooksService) {
        this.bookService = bookService;
        this.userService = userService;
        this.commentService = commentService;
        this.imageRepository = imageRepository;
        this.usersBooksService = usersBooksService;
    }


    @GetMapping({"/booksList", "/"})
    public String showBooks(Model model){

        String keyword = null;
        return findPaginated(1, model, "id", "asc", keyword, true);
//        return "books/booksList";
    }


    @Secured("ROLE_ADMIN")
    @GetMapping("/notListedBooks")
    public String getNotListedBooks(Model model){
        String keyword = null;
        return findPaginated(1, model, "id", "asc", keyword, false);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                Model model,
                                @Param("sortField") String sortField,
                                @Param("sortDir") String sortDir,
                                @Param("keyword") String keyword,
                                @Param("onTheList") boolean onTheList){
        int pageSize = 2;

        Page<Book> page = bookService.findPaginated(pageNo, pageSize, sortField, sortDir, keyword, onTheList);
        List<Book> listBooks = page.getContent();
        model.addAttribute("image", new Book());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalBooks", page.getTotalElements());
        model.addAttribute("books", listBooks);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("onTheList", onTheList);
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "books/booksList";
    }


    @GetMapping("/book/{id}")
    public String showBook(@PathVariable int id, Model model){
        model.addAttribute("book", bookService.getBook(id));
        model.addAttribute("comment", new Comment());
        if (userService.getCurrentUser() != null)
            model.addAttribute("user", userService.getCurrentUser());
        return "books/bookDetails";
    }

    @Secured("ROLE_ADMIN")
    @CrossOrigin
    @GetMapping("/addBook")
    public String addBookForm(Model model){
        model.addAttribute("book", new Book());
        return "books/addBook";
    }

//    @CrossOrigin
    @PostMapping("/addBook")
//    @InitBinder
    public String addBook(@ModelAttribute("book") @Valid Book toSave, BindingResult bindingResult,
                          @RequestParam("im") MultipartFile multipartFile,
                          RedirectAttributes redirectAttributes) throws IOException {
//        System.out.println("sadasd22");
//        DataBinder dataBinder = new DataBinder(multipartFile);
//        dataBinder.setDisallowedFields("image");
//        toSave.setImage(multipartFile.getBytes());
        if (bindingResult.hasFieldErrors())
            return "books/addBook";
        String fileName = "";
        if (multipartFile.isEmpty()){
            Path path = Paths.get(System.getProperty("user.dir") + "/book-photos/default.jpg");
            System.out.println(path);
//            File defaultPhoto = new File(System.getProperty("user.dir") + "/book-photos/default.jpg");
//            FileInputStream input = new FileInputStream(defaultPhoto);
            byte[] content = null;
            try {
                content = Files.readAllBytes(path);
            } catch (final IOException e) {
            }
            multipartFile = new MockMultipartFile("default.jpg", "default.jpg", "image/jpeg",content);
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getName()));
            System.out.println(fileName);
            Random rand = new Random();
            fileName = rand.nextInt(999999) + fileName;
            toSave.setImage(fileName);
        }
        else{
            System.out.println(multipartFile.getName());
            System.out.println(multipartFile.getOriginalFilename());
            System.out.println(multipartFile.getContentType());
//            System.out.println(multipartFile.ge);
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            System.out.println(fileName);
            Random rand = new Random();
            fileName = toSave.getId() + "" + rand.nextInt(999999) + fileName;
            toSave.setImage(fileName);
        }
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        System.out.println(fileName);
//        Random rand = new Random();
//        fileName = toSave.getId() + "" + rand.nextInt(999999) + fileName;
//        toSave.setImage(fileName);
        bookService.saveBook(toSave);
        String uploadDir = "book-photos";

        bookService.saveImage(uploadDir, fileName, multipartFile);
//        Image image = new Image();
//        image.setName(multipartFile.getName());
//        image.setContent(multipartFile.getBytes());
//        toSave.setImage(image);
//        imageRepository.save(image);
//        toSave.setImage(multipartFile.getBytes());
        bookService.saveBook(toSave);

//        bookService.saveImage(multipartFile, fileName, toSave.getId());

        return "redirect:/booksList";
    }

//    @CrossOrigin
    @PostMapping(value = "/addBook", params = "addAuthor")
    public String addAuthor(@ModelAttribute("book") Book current, @RequestParam("im") MultipartFile multipartFile) throws IOException {
        System.out.println("sadasd");
//        current.setImage(multipartFile.getBytes());
        current.getAuthors().add(new Author());
        System.out.println("sadasd");
        return "books/addBook";
    }

    @PostMapping(value = "/addBook", params = "deleteAuthor")
    public String deleteAuthor(@ModelAttribute("book") Book current, @RequestParam("im") MultipartFile multipartFile) throws IOException {
        System.out.println("sadasd");
//        current.setImage(multipartFile.getBytes());
        if (current.getAuthors().size() > 0)
            current.getAuthors().remove(current.getAuthors().size() - 1);
        System.out.println("sadasd");
        return "books/addBook";
    }

    @Transactional
    @PostMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable int id){
        bookService.deleteBookFromListById(id);
        return "redirect:/booksList";
    }

    @Transactional
    @PostMapping("/addToList/{id}")
    public String addToList(@PathVariable int id){
        Book book = bookService.getBook(id);
        book.setOnTheList(true);
        return "redirect:/booksList";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/updateBook/{id}")
    public String updateBookForm(@PathVariable int id, Model model){
//        MultipartFile file = new BASE64DecodedMultipartFile(bookService.getBook(id).getImage());
        Book stare = bookService.getBook(id);
        System.out.println("size " + stare.getAuthors().size());
        if (id < 0)
            id = -id;
        Book original = bookService.getBook(id);
        System.out.println("size " + original.getAuthors().size());
        Book copy = new Book();
        copy.setImage(original.getImage());
        copy.setQuantity(original.getQuantity());
        copy.setAuthors(original.getAuthors());
        if (copy.getAuthors().size() < original.getAuthors().size())
            copy.getAuthors().add(new Author());
        else if (copy.getAuthors().size() > original.getAuthors().size())
            copy.getAuthors().remove(copy.getAuthors().size() - 1);
        copy.setCategory(original.getCategory());
        copy.setDescription(original.getDescription());
        copy.setTitle(original.getTitle());
        copy.setNumOfPages(original.getNumOfPages());
        copy.setId(-original.getId());
        model.addAttribute("bookToUpdate", copy);
//        model.addAttribute("oldImage", bookService.getBook(id).getImage());
//        model.addAttribute("imm", file);
        return "books/updateBook";
    }

    @PostMapping(value = "/updateBook/{id}", params = "addAuthor")
    public String addAuthorUpdate(@PathVariable int id, @ModelAttribute("bookToUpdate") Book current, @RequestParam("im") MultipartFile multipartFile) throws IOException {
        System.out.println("sadasd");
//        current.setImage(multipartFile.getBytes());
        current.getAuthors().add(new Author());
//        if (current.getId() < 0) {
        Book toEdit = bookService.getBook(-id);
//        }
        toEdit.getAuthors().add(new Author());

//        id = toEdit.getId();
        System.out.println("toedit "+ toEdit.getId());
        System.out.println("sizeedit " + toEdit.getAuthors().size());
        System.out.println(id);
        id = -id;
//        current.setId(-id);
//        id = -id;
//        System.out.println("id przed " + id);
//        bookService.saveBook(toEdit);
        return "books/updateBook";
    }

    @PostMapping(value = "/updateBook/{id}", params = "deleteAuthor")
    public String deleteAuthorUpdate(@PathVariable int id, @ModelAttribute("bookToUpdate") Book current, @RequestParam("im") MultipartFile multipartFile) throws IOException {
        System.out.println("sadasd");
//        current.setImage(multipartFile.getBytes());
        Book toEdit = bookService.getBook(-id);
        if (current.getAuthors().size() > 0)
            current.getAuthors().remove(current.getAuthors().size() - 1);
        System.out.println("sadasd");
        id = -id;
//        current.setId(id);
//        bookService.saveBook(current);
        return "books/updateBook";
    }

    @Transactional
    @PostMapping(value = "/updateBook/{id}")
    public String updateBook(@ModelAttribute("bookToUpdate") @Valid Book toSave, BindingResult bindingResult,
                              @PathVariable int id, @RequestParam("im") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors ) {
                System.out.println (error.getObjectName() + " - " + error.getDefaultMessage());
            }
            return "redirect:/updateBook/{id}";
        }

        if (!multipartFile.isEmpty()) {
            System.out.println("asdasdsa");
//            Path path = Paths.get(bookService.getBook(-id).getPhotosImagePath());
            File oldPhoto = new File(System.getProperty("user.dir") + bookService.getBook(-id).getPhotosImagePath());
//            System.out.println(bookService.getBook(-id).getPhotosImagePath());
            oldPhoto.delete();
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            Random rand = new Random();
            fileName = rand.nextInt(999999) + fileName;
            toSave.setImage(fileName);
//            bookService.saveBook(toSave);
            String uploadDir = "book-photos";

            bookService.saveImage(uploadDir, fileName, multipartFile);
//            toSave.setImage(multipartFile.getBytes());
        }
        else
            toSave.setImage(bookService.getBook(-id).getImage());
        if (bindingResult.hasFieldErrors())
            return "redirect:/updateBook/{id}";
        Book oldBook = bookService.getBook(-id);
        oldBook.setImage(toSave.getImage());
        oldBook.setCategory(toSave.getCategory());
        oldBook.setDescription(toSave.getDescription());
        oldBook.setAuthors(toSave.getAuthors());
        oldBook.setTitle(toSave.getTitle());
        oldBook.setQuantity(toSave.getQuantity());
        oldBook.setNumOfPages(toSave.getNumOfPages());
        bookService.saveBook(oldBook);
        return "redirect:/booksList";
    }

    @Transactional
    @PostMapping("/borrowBook/{id}")
    public String borrowBook(@PathVariable int id){
        System.out.println("sadasdsa");
        User user = userService.getCurrentUser();
        System.out.println(user.getEmail());
        Book bookToBorrow = bookService.getBook(id);
        UsersBooks usersBooks = null;
        if (usersBooksService.existsByUserAndBookAndBorrowed(user, bookToBorrow)) {
//            usersBooks = usersBooksService.getUsersBooksByUserAndBook(user, bookToBorrow);
//            if (usersBooks.isBorrowed())
            return "redirect:/book/{id}";
        }
        else{
            usersBooks = new UsersBooks();
            usersBooks.setUser(user);
            usersBooks.setBook(bookToBorrow);
        }
        System.out.println(bookToBorrow.getQuantity());
        bookToBorrow.setQuantity(bookToBorrow.getQuantity() - 1);
        System.out.println(bookToBorrow.getQuantity());
        bookService.saveBook(bookToBorrow);
        usersBooks.setReturned(false);
        usersBooks.setBorrowed(true);
        usersBooks.setDeadlineForReturn(LocalDateTime.now().plusMonths(1));
        bookToBorrow.getUsers().add(usersBooks);
        user.getBooks().add(usersBooks);
        usersBooksService.saveUsersBooks(usersBooks);

//        borrowedBook.setDateOfReturn(LocalDateTime.now().plusMonths(1));
        bookService.saveBook(bookToBorrow);
        return "redirect:/book/{id}";
    }

    @PostMapping("/rateBook/{id}")
    public String rateBook(@PathVariable int id, @RequestParam("rate") int rating){
        User user = userService.getCurrentUser();
        Book book = bookService.getBook(id);
        UsersBooks usersBooks = null;
        if (usersBooksService.getRatedBooks(book, user).size() > 0){
//            return "redirect:/book/{id}";
            System.out.println("sdfsd");
            List<UsersBooks> usersBooks1 = usersBooksService.getRatedBooks(book, user);
            usersBooks = usersBooks1.stream().findFirst().orElse(null);
        }
        else {
            usersBooks = new UsersBooks();
            usersBooks.setUser(user);
            usersBooks.setBook(book);
            usersBooks.setBorrowed(false);
            book.getUsers().add(usersBooks);
            user.getBooks().add(usersBooks);
            usersBooksService.saveUsersBooks(usersBooks);
        }
        if (usersBooks.getRating() == 0){
            book.setNumOfVotes(book.getNumOfVotes() + 1);
        }
        else {
            double oldRating = usersBooks.getRating();
            if (book.getNumOfVotes() > 1)
                book.setRating((book.getRating() * book.getNumOfVotes() - oldRating) / (book.getNumOfVotes() -1));
        }
        usersBooks.setRating(rating);
        usersBooksService.saveUsersBooks(usersBooks);
        System.out.println(rating);
        double number = (book.getRating() * (book.getNumOfVotes() - 1) + rating) / book.getNumOfVotes();
        number = Math.round(number * 100);
        number = number/100;
        book.setRating(number);
        bookService.saveBook(book);
        return "redirect:/book/{id}";
    }

    @PostMapping("/reserveBook/{id}")
    public String reserveBook(@PathVariable("id") int id){
        User user = userService.getCurrentUser();
        Book book = bookService.getBook(id);
        if (user.getQueueForBooks().contains(book))
            return "redirect:/book/{id}";
        user.getQueueForBooks().add(book);
        book.getPeopleInQueue().add(user);
        bookService.saveBook(book);
        userService.saveUser(user);
        return "redirect:/book/{id}";
    }

    @Transactional
    public void borrowBook(int id, User user){
        System.out.println("sadasdsa");
        System.out.println(user.getEmail());
        Book bookToBorrow = bookService.getBook(id);
        UsersBooks usersBooks = null;
        if (usersBooksService.existsByUserAndBookAndBorrowed(user, bookToBorrow)) {
//            usersBooks = usersBooksService.getUsersBooksByUserAndBook(user, bookToBorrow);
//            if (usersBooks.isBorrowed())
                return;
        }
        else{
            usersBooks = new UsersBooks();
            usersBooks.setUser(user);
            usersBooks.setBook(bookToBorrow);
        }
        bookService.saveBook(bookToBorrow);
        usersBooks.setReturned(false);
        usersBooks.setBorrowed(true);
        usersBooks.setDeadlineForReturn(LocalDateTime.now().plusMonths(1));
        bookToBorrow.getUsers().add(usersBooks);
        user.getBooks().add(usersBooks);
        usersBooksService.saveUsersBooks(usersBooks);

//        borrowedBook.setDateOfReturn(LocalDateTime.now().plusMonths(1));
        bookService.saveBook(bookToBorrow);
    }

    @PostMapping("/returnBook/{id}")
    public String returnBook(@PathVariable("id") int id){
        System.out.println("dsfdsfdsfdsfdsf");
        User user = userService.getCurrentUser();
        Book book = bookService.getBook(id);
        UsersBooks usersBooks = usersBooksService.getUsersBooksByUserAndBookAndBorrowed(user, book);
        usersBooks.setBorrowed(false);
        usersBooks.setReturned(true);
        usersBooks.setDateOfReturn(LocalDateTime.now());
        if (book.getPeopleInQueue().size() > 0){
            User borrowingUser = book.getPeopleInQueue().stream().findFirst().orElse(null);
            if (borrowingUser != null) {
                borrowBook(id, borrowingUser);
                book.getPeopleInQueue().remove(borrowingUser);
                borrowingUser.getQueueForBooks().remove(book);
                bookService.saveBook(book);
                userService.saveUser(borrowingUser);
            }
        }
        else {
            book.setQuantity(book.getQuantity() + 1);
        }
        usersBooksService.saveUsersBooks(usersBooks);
        return "redirect:/profile";
    }

    @PostMapping("/addQuantity/{id}")
    public String addQuantity(@PathVariable("id") int id){
        Book book = bookService.getBook(id);
        if (book.getPeopleInQueue().size() > 0){
            User borrowingUser = book.getPeopleInQueue().stream().findFirst().orElse(null);
            if (borrowingUser != null) {
                borrowBook(id, borrowingUser);
                book.getPeopleInQueue().remove(borrowingUser);
                borrowingUser.getQueueForBooks().remove(book);
                bookService.saveBook(book);
                userService.saveUser(borrowingUser);
            }
        }
        else {
            book.setQuantity(book.getQuantity() + 1);
        }
        bookService.saveBook(book);
        return "redirect:/book/{id}";
    }

    @PostMapping("/subtractQuantity/{id}")
    public String subtractQuantity(@PathVariable("id") int id){
        Book book = bookService.getBook(id);
        book.setQuantity(book.getQuantity() - 1);
        bookService.saveBook(book);
        return "redirect:/book/{id}";
    }

//    @GetMapping("/bookUsers/{id}")
//    public String bookUsers(@PathVariable("id") int id, Model model){
//        Book book = bookService.getBook(id);
//        List<UsersBooks> usersBooks = usersBooksService.getUsersBooksByBookAndBorrowed(book, true);
//        model.addAttribute("usersBooks", usersBooks);
//        return "books/bookUsers";
//
//    }
}
