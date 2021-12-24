package projekt.biblioteka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.models.Message;
import projekt.biblioteka.models.Role;
import projekt.biblioteka.models.User;
import projekt.biblioteka.repositories.RoleRepository;
import projekt.biblioteka.repositories.UserRepository;
import projekt.biblioteka.services.BookService;
import projekt.biblioteka.services.CommentService;
import projekt.biblioteka.services.MessageService;
import projekt.biblioteka.services.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    private final BookService bookService;
    private final UserService userService;
    private final CommentService commentService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageService messageService;

    public UserController(BookService bookService, UserService userService, CommentService commentService, UserRepository userRepository, RoleRepository roleRepository, MessageService messageService) {
        this.bookService = bookService;
        this.userService = userService;
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.messageService = messageService;
    }

    //    @Secured("ROLE_USER")
//    @GetMapping("/login")
//    public String loginUser(){
//        return "index";
//    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "user/registrationForm";
    }

    @PostMapping("/processRegister")
    public String processRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model){
        if (bindingResult.hasFieldErrors()){
            return "user/registrationForm";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("message", "Istnieje użytkownik o podanym emailu");
            return "user/registrationForm";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        user.addRole(roleUser);
        userRepository.save(user);
        model.addAttribute("message", "Zarejestrowano się poprawnie");
        return "index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_MOD"})
    @GetMapping("/profile")
    public String showUserProfile(Model model){
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
//        model.addAttribute("book", new)
//        model.addAttribute("sender", 3);
//        model.addAttribute("message", new Message());
        model.addAttribute("sender", user);
        model.addAttribute("borrowedBooks", userService.getBorrowedBooks(user));
        model.addAttribute("returnedBooks", userService.getReturnedBooks(user));
        model.addAttribute("reservedBooks", user.getQueueForBooks());
        return "user/userProfile";
    }


    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable("id") int id, Model model){
        User user = userService.getUserById(id);
        User sender = userService.getCurrentUser();
        System.out.println(user.getEmail());
        model.addAttribute("user", user);
        model.addAttribute("sender", sender);
        model.addAttribute("message", new Message());
        model.addAttribute("borrowedBooks", userService.getBorrowedBooks(user));
        model.addAttribute("returnedBooks", userService.getReturnedBooks(user));
        model.addAttribute("reservedBooks", user.getQueueForBooks());
//        model.addAttribute("book", new)
//        model.addAttribute("books", bookService.getBooks());
        return "user/userProfile";
    }

    @PostMapping("/sendMessage/{id}")
    public String sendMessage(@PathVariable("id") int id, @ModelAttribute("message") Message message){
        User receiver = userService.getUserById(id);
        User sender = userService.getCurrentUser();
        message.setReceiverId(receiver);
        message.setSenderId(sender);
        message.setDate(LocalDateTime.now());
        messageService.saveMessage(message);
        return "redirect:/profile/{id}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_MOD"})
    @GetMapping("/messages/{id}")
    public String showMessages(@PathVariable("id") int id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("messagesSent", user.getMessagesSent());
        model.addAttribute("messagesReceived", user.getMessagesReceived());
        return "user/messages";
    }

    @PostMapping("/deleteReservation/{id}")
    public String deleteReservation(@PathVariable("id") int id){
        Book book = bookService.getBook(id);
        User user = userService.getCurrentUser();
        book.getPeopleInQueue().remove(user);
        bookService.saveBook(book);
        userService.saveUser(user);
        return "redirect:/profile";
    }
}
