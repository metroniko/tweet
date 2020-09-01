package ru.home.tweet.dto;

import ru.home.tweet.entity.Message;
import ru.home.tweet.entity.User;
import ru.home.tweet.util.MessageHelper;

public class MessageDTO {
    private Integer id;
    private User author;
    private String text;
    private String filename;
    private String tag;
    private Long likes;
    private Boolean meLiked;

    public MessageDTO(Message message, Long likes, Boolean meLiked) {
        this.id = message.getId();
        this.author = message.getAuthor();
        this.text = message.getText();
        this.filename = message.getFilename();
        this.tag = message.getTag();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public Integer getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getFilename() {
        return filename;
    }

    public String getTag() {
        return tag;
    }

    public Long getLikes() {
        return likes;
    }

    public Boolean getMeLiked() {
        return meLiked;
    }
    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }
}
