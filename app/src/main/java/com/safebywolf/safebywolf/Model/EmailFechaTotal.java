package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class EmailFechaTotal {
    String email;
    String fecha;
    Number total;
    @ServerTimestamp
    private Date timestamp;

    public EmailFechaTotal() {
    }

    public EmailFechaTotal(String email, String fecha, Number total) {
        this.email = email;
        this.fecha = fecha;
        this.total = total;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
