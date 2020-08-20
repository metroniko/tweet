package ru.home.tweet.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.home.tweet.entity.Role;
import ru.home.tweet.entity.User;
import ru.home.tweet.repository.UserRepo;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {


    private static  final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {


        log.info("registered user: {}", user);

        User userFromDB = userRepo.findByUsername(user.getUsername());

        if (userFromDB != null) {
            model.put("message", "User exist!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);


        return "redirect:/login";
    }
}
