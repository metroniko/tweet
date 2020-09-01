package ru.home.tweet.util;

import ru.home.tweet.entity.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername(): "none";
    }
}
