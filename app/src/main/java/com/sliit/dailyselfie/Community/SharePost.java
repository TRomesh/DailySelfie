package com.sliit.dailyselfie.Community;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tharaka on 10/04/2016.
 */
public class SharePost {


    private String PostType;
    private String PostImage;
    private String PostDescription;
    private String PostedTime;
    @JsonIgnore
    private String PostID;


    public SharePost(){}


    public SharePost(String pid,String ptype,String pimage,String pdesc,String ptime){
        PostID=pid;
        PostType=ptype;
        PostImage=pimage;
        PostDescription=pdesc;
        PostedTime=ptime;


    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getPostType() {
        return PostType;
    }

    public void setPostType(String postType) {
        PostType = postType;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getPostDescription() {
        return PostDescription;
    }

    public void setPostDescription(String postDescription) {
        PostDescription = postDescription;
    }

    public String getPostedTime() {
        return PostedTime;
    }

    public void setPostedTime(String postedTime) {
        PostedTime = postedTime;
    }
}
