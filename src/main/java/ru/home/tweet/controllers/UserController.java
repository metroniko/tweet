package ru.home.tweet.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.home.tweet.entity.Role;
import ru.home.tweet.entity.User;
import ru.home.tweet.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static  final Logger log = LoggerFactory.getLogger(UserController.class);


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userlist";
    }

    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userSave(Model model,
                           @RequestParam String username,
                           @RequestParam Map<String, String> form,
                           @RequestParam("userId") User user) {




        userService.saveUser(username, form, user);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model,
                             @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }


    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ) {

        userService.updateUser(user,password, email);

        return "redirect:/user/profile";

    }

    @GetMapping("subscribe/{user}")
    public String subscribe(@PathVariable User user,
                            @AuthenticationPrincipal User currentUser) {
        User principal = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        userService.subscribe(currentUser, user);
        return "redirect:/user-messages/" + user.getId();
    }


    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(@PathVariable User user,
                            @AuthenticationPrincipal User currentUser) {

        userService.unsubscribe(currentUser, user);
        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(Model model,
                           @PathVariable User user,
                           @PathVariable String type) {

        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        //ПРОВЕРИТЬ РАБОТУ ПОДПИСОК
        return "subscriptions";
    }
}
