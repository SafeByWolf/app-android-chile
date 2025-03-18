package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class TimestampFiledValue {
    @ServerTimestamp
    Date timestamp;

    public TimestampFiledValue() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
