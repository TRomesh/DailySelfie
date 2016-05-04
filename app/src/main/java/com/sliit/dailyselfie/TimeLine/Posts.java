package com.sliit.dailyselfie.TimeLine;

/**
 * Created by Tharaka on 03/05/2016.
 */
public class Posts {

    private int Id;
    private String Description;
    private double Height;
    private double Weight;
    private double WaistSize;
    private String Created_at;
    private String challangeName;
    private String image;

    public Posts(int id, String description, double height, double weight, double waistSize, String created_at, String image, String challangeName) {
        Id = id;
        Description = description;
        Height = height;
        Weight = weight;
        WaistSize = waistSize;
        Created_at = created_at;
        this.challangeName = challangeName;
        this.image = image;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public double getWaistSize() {
        return WaistSize;
    }

    public void setWaistSize(double waistSize) {
        WaistSize = waistSize;
    }

    public String getCreated_at() {
        return Created_at;
    }

    public void setCreated_at(String created_at) {
        Created_at = created_at;
    }

    public String getChallangeName() {
        return challangeName;
    }

    public void setChallangeName(String challangeName) {
        this.challangeName = challangeName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
