package com.qrilt.page.model;

import java.util.Date;

public class Message {
    public String from;
    public String to;
    public String content;
    public Date timeStamp;

    public Message(String from, String to, String content, Date timeStamp) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.timeStamp = timeStamp;
    }
}
