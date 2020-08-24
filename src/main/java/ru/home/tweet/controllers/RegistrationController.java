package ru.home.tweet.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.home.tweet.dto.CaptchaResponseDTO;
import ru.home.tweet.entity.User;
import ru.home.tweet.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";


    private static  final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService;

    private final RestTemplate restTemplate;

    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }
    @Value("${recaptcha.secret}")
    private String secretKey;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
                @RequestParam("password2") String passwordConfirm,
                @RequestParam("g-recaptcha-response") String captchaResponse,
                @Valid  User user,
                BindingResult bindingResult,
                Model model) {


        String url = String.format(CAPTCHA_URL, "6LcHmMIZAAAAADuGjlAYIrQVFBIUrQ-ZjgA9Qg-z", captchaResponse);

        log.info("registered user {}, captchaResponse: {},  url: {}", user, captchaResponse, url);

        CaptchaResponseDTO response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDTO.class);
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Заполни каптчу");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Yt подиверждён");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Password are not defined");

            return "registration";
        }


        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);

            return "registration";

        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exist!");
            return "registration";
        }

        return "redirect:/loginMain";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {

        boolean isActivate = userService.isActivateUser(code);
        if (isActivate) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activate");
        } else {
            model.addAttribute("messageType", "danger");

            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
