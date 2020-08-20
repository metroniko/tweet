package ru.home.tweet.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.home.tweet.entity.Message;
import ru.home.tweet.entity.User;
import ru.home.tweet.repository.MessageRepo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private MessageRepo messageRepo;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String mainControl(Model model,
                              @RequestParam(required = false, defaultValue = "") String filter) {
        Iterable<Message> messageByTag;
        if (filter != null && !filter.isEmpty()) {
            messageByTag = messageRepo.findMessageByTag(filter);
        } else {
            messageByTag = messageRepo.findAll();
        }
        model.addAttribute("messages", messageByTag);
        model.addAttribute("filter", filter);
        return "main";
    }


    //AuthenticationPrincipal  - получаем текущего пользователя в качестве параметра
    @PostMapping("/main")
    public String addMessage(
                            @AuthenticationPrincipal User user,
                            @RequestParam String text,
                            @RequestParam String tag,
                            Map<String, Object> model) {



        Message message = new Message(text, tag, user);

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);
        model.put("filter", "");

        return "main";
    }


}