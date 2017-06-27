package com.adamzajdlik.firebasedemo.content;

import android.support.annotation.Nullable;

/**
 * Created by adamzajdlik on 2017-06-26.
 */

public class Message {
    private String author;
    private String content;
    private String attachmentUrl;

    public Message() {
    }

    public Message(String author, String content, @Nullable String attachmentUrl) {
        this.author = author;
        this.content = content;
        this.attachmentUrl = attachmentUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
