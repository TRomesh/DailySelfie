package com.sliit.dailyselfie.Community;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tharaka on 10/04/2016.
 */
public class RegisterUser {

    private String Uname;
    private String Password;
    private String Profilepic;

    @JsonIgnore
    private int key;

    public RegisterUser(){}


    public RegisterUser(String uname,String password,String propic){
        Uname=uname;
        Password=password;
        Profilepic=propic;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
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
