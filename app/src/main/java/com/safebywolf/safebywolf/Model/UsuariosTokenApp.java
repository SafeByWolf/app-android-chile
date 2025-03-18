package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UsuariosTokenApp {
    String email;

    @ServerTimestamp
    Date timestamp;

    public UsuariosTokenApp() {
    }

    public UsuariosTokenApp(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
