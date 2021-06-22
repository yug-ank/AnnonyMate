package com.xinicompany.annonymate;

public class SessionManager {
    public static String username = null;
    public static Boolean isGuest = false;
    SessionManager(String username , Boolean isGuest){
        SessionManager.username = username;
        SessionManager.isGuest = isGuest;
    }
}
