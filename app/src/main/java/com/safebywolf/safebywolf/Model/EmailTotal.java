package com.safebywolf.safebywolf.Model;

public class EmailTotal {
    Integer total;
    String email;

    public EmailTotal() {
    }

    public EmailTotal(String email, Integer total) {
        this.email = email;
        this.total = total;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
