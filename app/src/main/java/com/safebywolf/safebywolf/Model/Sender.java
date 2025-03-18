package com.safebywolf.safebywolf.Model;

public class Sender {
    String to;
    Notification notification;

    public Sender() {
    }

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }
}
