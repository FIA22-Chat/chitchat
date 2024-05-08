package io.github.chitchat.common;

import java.time.LocalDateTime;

public class Message {
    private Message type;
    private String content;
    private String from;
    private String to;
    private LocalDateTime uhrzeit;

    public Message(Message type, String from, String to, String content, LocalDateTime uhrzeit) {
        this.type = type;
        this.content = content;
    }

    public Message getType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }
}
