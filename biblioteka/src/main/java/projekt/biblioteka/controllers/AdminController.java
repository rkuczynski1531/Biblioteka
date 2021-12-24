package projekt.biblioteka.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projekt.biblioteka.models.Book;
import projekt.biblioteka.models.User;
import projekt.biblioteka.services.BookService;
import projekt.biblioteka.services.CommentService;
import projekt.biblioteka.services.RoleService;
import projekt.biblioteka.services.UserService;

import java.util.List;

@Controller
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final CommentService commentService;
    private final RoleService roleService;

    public AdminController(BookService bookService, UserService userService, CommentService commentService, RoleService roleService) {
        this.bookService = bookService;
        this.userService = userService;
        this.commentService = commentService;
        this.roleService = roleService;
    }

    @GetMapping("/adminPanel")
    public String showAdminPanel(Model model){
        return "admin/adminPanel";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/adminPanel/usersList")
    public String showUsers(Model model){
        String keyword = null;
        return findPaginated(1, model, keyword);
//        model.addAttribute("users", userService.getUsers());
//        model.addAttribute("roles", roleService.getRoles());
//        return "admin/usersList";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/usersList/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                Model model,
                                @Param("keyword") String keyword){
        int pageSize = 2;

        Page<User> page = userService.findPaginated(pageNo, pageSize, keyword);
        List<User> listUsers = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalUsers", page.getTotalElements());
        model.addAttribute("users", listUsers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("roles", roleService.getRoles());

        return "admin/usersList";
    }

    @PostMapping("/addRole/{userId}/{roleId}")
    public String addRole(@PathVariable int userId, @PathVariable int roleId){
        User user = userService.getUserById(userId);
        user.addRole(roleService.getRoleById(roleId));
        userService.saveUser(user);
        return "redirect:/adminPanel/usersList";
    }

    @PostMapping("/deleteRole/{userId}/{roleId}")
    public String deleteRole(@PathVariable int userId, @PathVariable int roleId){
        User user = userService.getUserById(userId);
        User currentUser = userService.getCurrentUser();
        user.deleteRole(roleService.getRoleById(roleId));
        userService.saveUser(user);
        if (user == currentUser){
            if (roleId == 2)
                return "redirect:/logout";
        }
        return "redirect:/adminPanel/usersList";
    }

    @GetMapping("/adminPanel/user/{id}")
    public String showUserDetails(@PathVariable int id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("books", userService.getUserById(id).getBooks());
        return "/admin/userDetails";
    }

    @Secured({"ROLE_ADMIN", "ROLE_MOD"})
    @GetMapping("/adminPanel/commentsList")
    public String showComments(Model model){
        model.addAttribute("comments", commentService.getAll());
        return "/admin/commentsList";
    }


}
