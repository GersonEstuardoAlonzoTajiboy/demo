package com.example.demo.models;

/**
 * Singleton to store the current user session.
 */
public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();
    private UserModel currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }
}
