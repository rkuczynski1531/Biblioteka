package projekt.biblioteka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import projekt.biblioteka.models.Role;
import projekt.biblioteka.models.User;
import projekt.biblioteka.repositories.RoleRepository;
import projekt.biblioteka.repositories.UserRepository;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
}
