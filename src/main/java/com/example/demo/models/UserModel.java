package com.example.demo.models;

import javafx.beans.property.*;

public class UserModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final ObjectProperty<RoleModel> role = new SimpleObjectProperty<>();

    public UserModel(int id, String fullName, String email, String password, RoleModel role) {
        this.id.set(id);
        this.fullName.set(fullName);
        this.email.set(email);
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

    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
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
