package com.sliit.dailyselfie.Community;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tharaka on 10/04/2016.
 */
public class RegisterUser {

    private String fname;
    private String Lname;
    private String Password;
    private String Email;
    private String Profilepic;

    @JsonIgnore
    private int key;

    public RegisterUser(){}


    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public RegisterUser(String uname, String password, String propic){
        fname=uname;
        Password=password;
        Profilepic=propic;

    }

    public String getUname() {
        return fname;
    }

    public void setUname(String uname) {
        fname = uname;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getProfilepic() {
        return Profilepic;
    }

    public void setProfilepic(String profilepic) {
        Profilepic = profilepic;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
