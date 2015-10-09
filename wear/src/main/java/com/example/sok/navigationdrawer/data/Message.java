package com.example.sok.navigationdrawer.data;

import java.util.Date;

public class Message {
    private String author;
    private String text;
    private Date date;

    public Message(String author, String text, Date date) {
        this.author = author;
        this.text = text;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
