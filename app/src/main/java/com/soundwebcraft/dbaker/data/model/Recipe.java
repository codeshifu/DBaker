package com.soundwebcraft.dbaker.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("ingredients")
    public List<Ingredients> ingredients;
    @SerializedName("steps")
    public List<Steps> steps;
    @SerializedName("servings")
    public int servings;
    @SerializedName("image")
    public String image;

    public static class Ingredients {
        @SerializedName("quantity")
        public double quantity;
        @SerializedName("measure")
        public String measure;
        @SerializedName("ingredient")
        public String ingredient;
    }

    public static class Steps {
        @SerializedName("id")
        public int id;
        @SerializedName("shortDescription")
        public String shortdescription;
        @SerializedName("description")
        public String description;
        @SerializedName("videoURL")
        public String videourl;
        @SerializedName("thumbnailURL")
        public String thumbnailurl;
    }
}
