package io.github.chitchat.common.database.models;

public class User {
    private int id;
    private String name;
    // private HashSHA256 password;
    private String salt;
    private String email;

    /*    public User(int id, String name, String email, HashSHA256 password, String salt)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.salt = salt;
    } */
}
