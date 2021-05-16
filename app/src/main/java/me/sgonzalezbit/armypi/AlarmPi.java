package me.sgonzalezbit.armypi;

import java.io.Serializable;

public class AlarmPi implements Serializable {

    private String alarmId;
    private String alarmName;
    private String uidUser;
    private String urlAccess;
    private boolean Active;
    private String username;
    private String password;

    public AlarmPi(){
    }

    public AlarmPi(String alarmId, String alarmName, String uidUser, String urlAccess, boolean Active, String username, String password) {
        this.alarmId = alarmId;
        this.alarmName = alarmName;
        this.uidUser = uidUser;
        this.urlAccess = urlAccess;
        this.Active = Active;
        this.username = username;
        this.password = password;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getUrlAccess() {
        return urlAccess;
    }

    public void setUrlAccess(String urlAccess) {
        this.urlAccess = urlAccess;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
