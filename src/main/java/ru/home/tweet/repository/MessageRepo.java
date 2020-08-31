package ru.home.tweet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.tweet.entity.Message;

@Repository
public interface MessageRepo extends CrudRepository<Message, Integer> {

    Page<Message> findMessageByTag(String tag, Pageable pageable);

    Page<Message> findAll(Pageable pageable);
}
