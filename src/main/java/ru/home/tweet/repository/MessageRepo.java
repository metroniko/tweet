package ru.home.tweet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.tweet.entity.Message;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer> {

    List<Message> findMessageByTag(String tag);
}
