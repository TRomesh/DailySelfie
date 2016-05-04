package com.sliit.dailyselfie.Community;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Tharaka on 10/04/2016.
 */

@JsonIgnoreProperties(ignoreUnknown =true)
public class SharePost {

    private String PostSharer;
    private String PostType;
    private String PostImage;
    private String PostDescription;
    private String PostedTime;
    @JsonIgnore
    private String PostID;


    public SharePost(){}

    public SharePost(String postSharer, String postType, String postImage, String postDescription, String postedTime, String postID) {
        PostSharer = postSharer;
        PostType = postType;
        PostImage = postImage;
        PostDescription = postDescription;
        PostedTime = postedTime;
        PostID = postID;
    }


    public String getPostSharer() {
        return PostSharer;
    }

    public void setPostSharer(String postSharer) {
        PostSharer = postSharer;
    }

//    public String getPostID() {
//        return PostID;
//    }
//
//    public void setPostID(String postID) {
//        PostID = postID;
//    }

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
