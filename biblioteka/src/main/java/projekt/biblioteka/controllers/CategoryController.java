package projekt.biblioteka.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekt.biblioteka.models.Category;
import projekt.biblioteka.repositories.ImageRepository;
import projekt.biblioteka.services.*;

import java.util.Comparator;
import java.util.List;

@Controller
public class CategoryController {

    private final BookService bookService;
    private final UserService userService;
    private final CommentService commentService;
    private final ImageRepository imageRepository;
    private final UsersBooksService usersBooksService;
    private final CategoryService categoryService;

    public CategoryController(BookService bookService, UserService userService, CommentService commentService, ImageRepository imageRepository, UsersBooksService usersBooksService, CategoryService categoryService) {
        this.bookService = bookService;
        this.userService = userService;
        this.commentService = commentService;
        this.imageRepository = imageRepository;
        this.usersBooksService = usersBooksService;
        this.categoryService = categoryService;
    }
    @GetMapping("/addNewCategory")
    public String addNewCategory(Model model){
        model.addAttribute("newCategory", new Category());
        List<Category> categs = categoryService.findAllWithoutParent();
        for (Category cat : categs){
            cat.getChildCategories().sort(Comparator.comparing(Category::getName));
        }
        model.addAttribute("categories", categs);
        return "/books/addNewCategory";
    }

    @PostMapping("/addNewCategory")
    public String addNewCategory(@ModelAttribute("newCategory") Category newCategory){
        if (categoryService.existsByName(newCategory.getName()))
            return "/books/addNewCategory";
        categoryService.saveCategory(newCategory);
        return "redirect:/addBook";
    }

    @PostMapping(value = "/addNewCategory", params = "add")
    public String addNewCategory(@ModelAttribute("newCategory") Category newCategory, @RequestParam("newSubCat") String categoryName, Model model){
        List<Category> categs = categoryService.findAllWithoutParent();
        for (Category cat : categs){
            cat.getChildCategories().sort(Comparator.comparing(Category::getName));
        }
        model.addAttribute("categories", categs);
        if (categoryService.existsByName(newCategory.getName())) {
            return "/books/addNewCategory";
        }
        newCategory.setParentCategory(categoryService.getCategoryByName(categoryName));
        System.out.println(newCategory.getName());
        System.out.println(categoryName);
        categoryService.saveCategory(newCategory);
        return "redirect:/addBook";
    }
}
