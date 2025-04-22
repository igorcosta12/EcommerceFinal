package com.unicesumar.entities;

import java.util.UUID;

public class User extends Entity {
    private int id;
    private String name;
    private String email;
    private String password;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(UUID uuid, String name, String email, String password) {
        super(uuid);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return String.format("%s - %s", this.name, this.email);
    }
}
