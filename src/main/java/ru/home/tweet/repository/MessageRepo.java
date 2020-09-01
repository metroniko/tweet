package ru.home.tweet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.tweet.dto.MessageDTO;
import ru.home.tweet.entity.Message;
import ru.home.tweet.entity.User;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer> {

    Page<Message> findMessageByTag(String tag, Pageable pageable);

    Page<Message> findAll(Pageable pageable);


    Page<MessageDTO> findByTag(String filter, Pageable pageable, User user);

    Page<MessageDTO> findByUser(Pageable pageable, User author, User currentUser);

    Page<MessageDTO> findAll(Pageable pageable, User user);
}
