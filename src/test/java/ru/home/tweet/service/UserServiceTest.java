package ru.home.tweet.service;

import ru.home.tweet.entity.Role;
import ru.home.tweet.entity.User;
import ru.home.tweet.repository.UserRepo;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Collections;



@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;


    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    void addUser() {
        User newUser = new User();
        newUser.setEmail("some@yandex.ru");

        boolean addUser = userService.addUser(newUser);

        Assert.assertTrue(addUser);
        Assert.assertNotNull(newUser.getActivationCode());

        Assert.assertTrue(CoreMatchers.is(newUser.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(newUser);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(newUser.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );

    }
    @Test
    void addUserFailedTest() {
        User user = new User();
        user.setUsername("Kolya");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Kolya");
        boolean isSaved = userService.addUser(user);
        Assert.assertFalse(isSaved);

        Mockito.verify(userRepo, Mockito.times(0)).save(user);
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );


    }

    @Test
    void isActivateUser() {

        User user = new User();
        user.setActivationCode("bb");

        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("active");

        boolean active = userService.isActivateUser("active");
        Assert.assertTrue(active);
        Assert.assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);

    }
    @Test
    void activateUserFailedTest() {
        boolean active = userService.isActivateUser("activem");

        Assert.assertFalse(active);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));


    }
}