package me.sgonzalezbit.armypi;

import java.io.Serializable;

public class Notification implements Serializable {
    public String date;
    public String message;

    public Notification(String date) {
        this.date = date;
        this.message = "Motion detected";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
