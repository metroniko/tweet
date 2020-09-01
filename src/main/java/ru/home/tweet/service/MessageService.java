package ru.home.tweet.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.home.tweet.dto.MessageDTO;
import ru.home.tweet.entity.User;
import ru.home.tweet.repository.MessageRepo;

import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

@Service
public class MessageService {

    private EntityManager entityManager;
    private MessageRepo messageRepo;

    public MessageService(EntityManager entityManager,
                          MessageRepo messageRepo) {
        this.entityManager = entityManager;
        this.messageRepo = messageRepo;
    }



    public Page<MessageDTO> messageList(Pageable pageable, String filter, User user) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepo.findByTag(filter, pageable, user);
        } else {
            return messageRepo.findAll(pageable, user);
        }
    }

    public Page<MessageDTO> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepo.findByUser(pageable, author, currentUser);
    }
}
