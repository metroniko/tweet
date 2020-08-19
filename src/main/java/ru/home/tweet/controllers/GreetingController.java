package ru.home.tweet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.home.tweet.entity.Message;
import ru.home.tweet.repository.MessageRepo;

import java.util.List;
import java.util.Map;

@Controller
public class GreetingController {

    private MessageRepo messageRepo;

    public GreetingController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/")
    public String mainControl(Map<String, Object> model) {
        Iterable<Message> all = messageRepo.findAll();
        model.put("messages", all);
        return "main";
    }

    @PostMapping("/greeting/addMessage")
    public String addMessage(@RequestParam String text,
                             @RequestParam String tag,
                              Map<String, Object> model) {

        Message message = new Message(text, tag);

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);

        return "main";
    }

    @PostMapping("greeting/filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {

        List<Message> messageByTag = messageRepo.findMessageByTag(filter);
        model.put("message", messageByTag);
        return "main";
    }

}