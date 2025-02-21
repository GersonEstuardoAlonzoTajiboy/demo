package com.example.demo.models;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Template for the login form.
 */
public class LoginModel {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final BooleanProperty valid = new SimpleBooleanProperty(false);

    public LoginModel() {
        valid.bind(Bindings.createBooleanBinding(() ->
                        getUsername() != null &&
                                !getUsername().trim().isEmpty() &&
                                getPassword() != null &&
                                !getPassword().trim().isEmpty(),
                username,
                password
        ));
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }
}
