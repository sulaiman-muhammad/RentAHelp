package com.example.rentahelp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {
    private String userId;
    private String message;
    private String time;

    public Notification() {
    }

    public Notification(String userId, String message) {
        this.userId = userId;
        this.message = message;
        this.time = getCurrentTime();
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String notificationId) {
        this.userId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
