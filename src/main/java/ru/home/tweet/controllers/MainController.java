package ru.home.tweet.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.home.tweet.entity.Message;
import ru.home.tweet.entity.User;
import ru.home.tweet.repository.MessageRepo;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

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
                            @RequestParam(name = "file") MultipartFile file,
                            Map<String, Object> model) throws IOException {


        Message message = new Message(text, tag, user);


        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuifFile = UUID.randomUUID().toString();
            String finalFile = uuifFile + "." +file.getOriginalFilename();

            message.setFilename(finalFile);

            file.transferTo(new File(uploadDir+"/"+finalFile));
        }

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);
        model.put("filter", "");

        return "main";
    }


}