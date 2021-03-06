package ru.home.tweet.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.home.tweet.dto.MessageDTO;
import ru.home.tweet.entity.Message;
import ru.home.tweet.entity.User;
import ru.home.tweet.repository.MessageRepo;
import ru.home.tweet.service.MessageService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {

    private MessageRepo messageRepo;

    private MessageService messageService;

    private static  final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Value("${upload.path}")
    private String uploadPath;

    public MessageController(MessageRepo messageRepo, MessageService messageService) {
        this.messageRepo = messageRepo;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        Page<MessageDTO> page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }


    //AuthenticationPrincipal  - получаем текущего пользователя в качестве параметра
    @PostMapping("/main")
    public String addMessage(
                            @AuthenticationPrincipal User user,
                            @Valid Message message,
                            BindingResult bindingResult,
                            @RequestParam(name = "file") MultipartFile file,
                            Model model) throws IOException {


        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);
        } else {


            saveFile(message, file);


            model.addAttribute("message", null);

            messageRepo.save(message);
        }
        Iterable<Message> messages = messageRepo.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("filter", "");

        return "main";
    }

    private void saveFile(@Valid Message message, @RequestParam(name = "file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuifFile = UUID.randomUUID().toString();
            String finalFile = uuifFile + "." + file.getOriginalFilename();

            message.setFilename(finalFile);

            file.transferTo(new File(uploadDir + "/" + finalFile));
        }
    }

    @GetMapping("/user-messages/{user}")
    //@Transactional
    public String getUserMessages(@AuthenticationPrincipal User currentUser,
                                  @PathVariable User user,
                                  @RequestParam(required = false) Message message,
                                  Model model) {

        log.info("current User: {}, message: {}", currentUser, message);

        Set<Message> messages = user.getMessages();
        model.addAttribute("messages", messages);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("isCurrentUser",currentUser.equals(user));

        return "userMessages";

    }

    @PostMapping("/user-messages/{user}")
    public String updateUserMessage(@AuthenticationPrincipal User currentUser,
                                    @PathVariable Long user,
                                    @RequestParam("id") Message message,
                                    @RequestParam("text") String text,
                                    @RequestParam("tag") String tag,
                                    @RequestParam("file") MultipartFile file
                                    ) throws IOException {



        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }

            saveFile(message, file);

            log.info("saved message : {}", message);

            messageRepo.save(message);
        }

        return String.format("redirect:/user-messages/%d", user);

    }

    @GetMapping("/messages/{message}/like")
    @Transactional
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "message") Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> likes = message.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }




}