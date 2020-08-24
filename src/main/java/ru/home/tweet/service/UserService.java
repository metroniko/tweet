package ru.home.tweet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.home.tweet.entity.Role;
import ru.home.tweet.entity.User;
import ru.home.tweet.repository.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private static  final Logger log = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepo userRepo,
                       MailSender mailSender,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user) {
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);


        if (!StringUtils.isEmpty(user.getEmail())) {

            sendActivationCode(user);
        }

        return true;
    }

    private void sendActivationCode(User user) {
        String message = String.format(
                "Hello, %s \n" +
                        "Welcome to thr club. Link: http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        mailSender.send(user.getEmail(), "Actiavtion message", message);
    }

    public boolean isActivateUser(String code) {

        User user = userRepo.findByActivationCode(code);
        log.info("founded user: {} ", user);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepo.save(user);

        return true;
    }

    public Iterable<User> findAll() {

        return userRepo.findAll();
    }

    public boolean saveUser(String username, Map<String, String> form, User user) {

        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key: form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        return true;
    }

    public void updateUser(User user, String password, String email) {

        String currentEmail = user.getEmail();
        boolean isEmailChanged = (email != null && !email.equals(currentEmail))
                || (currentEmail != null && currentEmail.equals(email));



        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }

        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);
        if (isEmailChanged) {
            sendActivationCode(user);
        }



    }
}
