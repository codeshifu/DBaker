package com.soundwebcraft.dbaker.data.model;

import com.google.gson.annotations.SerializedName;
import com.soundwebcraft.dbaker.R;

import org.parceler.Parcel;

import java.text.DecimalFormat;
import java.util.List;

@Parcel
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

    public Recipe() {
    }

    public Recipe(int id, String name, List<Ingredients> ingredients, List<Steps> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public int getImageResourceId() {
        String recipeName = getName().replaceAll("\\s", "").toLowerCase();
        switch (recipeName) {
            case "nutellapie":
                return R.drawable.nutellapie;
            case "cheesecake":
                return R.drawable.cheesecake;
            case "yellowcake":
                return R.drawable.yellowcake;
            case "brownies":
                return R.drawable.brownies;
            default:
                throw new IllegalArgumentException("Recipe image not found");
        }
    }

    @Parcel
    public static class Ingredients {
        @SerializedName("quantity")
        public double quantity;
        @SerializedName("measure")
        public String measure;
        @SerializedName("ingredient")
        public String ingredient;

        public Ingredients() {
        }

        public double getQuantity() {
            return quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public Ingredients(double quantity, String measure, String ingredient) {
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }

        @Override
        public String toString() {
            String ingr = this.getIngredient();
            ingr = Character.toUpperCase(ingr.charAt(0)) + ingr.substring(1);
            String measure = this.getMeasure();
            double qty = this.getQuantity();

            DecimalFormat df = new DecimalFormat("##.#");

            return ingr + ", " + df.format(qty) + " " + measure;
        }
    }

    @Parcel
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

        public Steps() {
        }

        public int getId() {
            return id;
        }

        public String getShortdescription() {
            return shortdescription;
        }

        public String getDescription() {
            return description;
        }

        public String getVideourl() {
            return videourl;
        }

        public String getThumbnailurl() {
            return thumbnailurl;
        }

        public Steps(int id, String shortdescription, String description, String videourl, String thumbnailurl) {
            this.id = id;
            this.shortdescription = shortdescription;
            this.description = description;
            this.videourl = videourl;
            this.thumbnailurl = thumbnailurl;
        }
    }
}
