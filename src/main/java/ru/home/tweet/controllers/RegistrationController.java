package ru.home.tweet.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.home.tweet.entity.User;
import ru.home.tweet.service.UserService;

import java.util.Map;

@Controller
public class RegistrationController {


    private static  final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {

        log.info("registered user: {}", user);

        if (!userService.addUser(user)) {
            model.put("message", "User exist!");
            return "registration";
        }

        return "redirect:/loginMain";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {

        boolean isActivate = userService.isActivateUser(code);
        if (isActivate) {
            model.addAttribute("message", "User successfully activate");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
