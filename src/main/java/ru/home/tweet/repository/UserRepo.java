package ru.home.tweet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.tweet.entity.User;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    User findByActivationCode(String activationCode);
}
