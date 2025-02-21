package com.example.demo.models;

import javafx.beans.property.*;

public class UserModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ObjectProperty<RoleModel> role = new SimpleObjectProperty<>();

    public UserModel(int id, String username, String password, RoleModel role) {
        this.id.set(id);
        this.username.set(username);
        this.password.set(password);
        this.role.set(role);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
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

    public RoleModel getRole() {
        return role.get();
    }

    public void setRole(RoleModel role) {
        this.role.set(role);
    }

    public ObjectProperty<RoleModel> roleProperty() {
        return role;
    }
}
