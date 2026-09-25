package com.example.projecto_1_chat.domain.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;

    public User(String id, String name, String email, String profileImageUrl){
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public User(){};

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return  email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;

    }

}


