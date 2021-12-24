package projekt.biblioteka.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.models.Comment;
import projekt.biblioteka.models.User;
import projekt.biblioteka.repositories.CommentRepository;
import projekt.biblioteka.services.BookService;
import projekt.biblioteka.services.CommentService;
import projekt.biblioteka.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class CommentController {
    private final BookService bookService;
    private final UserService userService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    public CommentController(BookService bookService, UserService userService, CommentService commentService,
                             CommentRepository commentRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

//    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE"})
    @PostMapping("/addComment/{bookId}")
    public String addComment(HttpServletRequest request, @PathVariable int bookId, @RequestParam("comment") String commentText){
        Book book = bookService.getBook(bookId);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
        User user = userService.getCurrentUser();
//        if (SecurityContextHolderAwareRequestWrapper.isUserInRole(String role))
        Comment comment = new Comment(commentText);
        comment.setUser(user);
        comment.setDate(LocalDateTime.now());
        comment.setBook(book);
        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_MOD"))
            comment.setAccepted(true);
        commentService.saveComment(comment);

        book.addComment(comment);
        bookService.saveBook(book);
        return "redirect:/book/{bookId}";
    }

    @PostMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable int id){
        Comment comment = commentService.findById(id);
        int bookId = comment.getBook().getId();
        commentService.deleteCommentById(id);
        return "redirect:/book/" + bookId;
    }

    @PostMapping("/acceptComment/{id}")
    public String acceptComment(@PathVariable int id){
        Comment comment = commentService.findById(id);
        comment.setAccepted(true);
        commentService.saveComment(comment);
        return "redirect:/adminPanel/commentsList";
    }


}
