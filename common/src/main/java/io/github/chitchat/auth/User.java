package io.github.chitchat.auth;

public class User
{
    private String username;
    private String email;

    public User(String username, String email)
    {
        System.out.println("neuer user angelegt");
        this.username = username;
        this.email = email;
    }

    public String getName()
    {
        return username;
    }

    public String getEmail()
    {
        return email;
    }
}
