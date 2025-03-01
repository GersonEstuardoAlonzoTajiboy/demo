package com.example.demo.models;

/**
 * Singleton to store the current user session.
 */
public class SessionManager {

    private static SessionManager instance;
    private UserModel currentUser;

    private SessionManager() {
        throw new UnsupportedOperationException("Model class. Do not instantiate!");
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser = currentUser;
    }
}
