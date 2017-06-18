package com.soundwebcraft.dbaker.db;

import com.orm.SugarRecord;

public class RecipeEntity extends SugarRecord<RecipeEntity> {
    int recipeId;
    String name;
    int servings;
    String image;

    public RecipeEntity() {
    }

    public RecipeEntity(int recipeId, String name, int servings, String image) {
        this.recipeId = recipeId;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public static class StepEntity extends SugarRecord<StepEntity> {
        int stepId;
        String shortdescription;
        String description;
        String videourl;
        String thumbnailurl;

        RecipeEntity recipe;

        public StepEntity() {
        }

        public StepEntity(int stepId, String shortdescription, String description, String videourl, String thumbnailurl, RecipeEntity recipe) {
            this.stepId = stepId;
            this.shortdescription = shortdescription;
            this.description = description;
            this.videourl = videourl;
            this.thumbnailurl = thumbnailurl;
            this.recipe = recipe;
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

        public String getThumbnailUrl() {
            return thumbnailurl;
        }

        public int getStepId() {
            return stepId;
        }
    }

    public static class IngredientEntity extends SugarRecord<IngredientEntity> {
        double quantity;
        String measure;
        String ingredient;

        RecipeEntity recipe;

        public IngredientEntity () {

        }

        public IngredientEntity(double quantity, String measure, String ingredient, RecipeEntity recipe) {
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
            this.recipe = recipe;
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
    }

}
