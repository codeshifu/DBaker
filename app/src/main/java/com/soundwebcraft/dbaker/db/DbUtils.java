package com.soundwebcraft.dbaker.db;

import com.soundwebcraft.dbaker.data.model.Recipe;

import java.util.List;

public class DbUtils {
    public static void saveStepEntities(List<Recipe.Steps> steps, RecipeEntity recipeEntity) {
        for (int i = 0; i < steps.size(); i++) {
            RecipeEntity.StepEntity stepEntity = new RecipeEntity.StepEntity(
                    steps.get(i).id,
                    steps.get(i).shortdescription,
                    steps.get(i).description,
                    steps.get(i).videourl,
                    steps.get(i).thumbnailurl,
                    recipeEntity
            );
            stepEntity.save();
        }
    }

    public static void saveIngredientEntities(List<Recipe.Ingredients> ingredients, RecipeEntity recipeEntity) {
        for (int i = 0; i < ingredients.size(); i++) {
            RecipeEntity.IngredientEntity ingredientEntity = new RecipeEntity.IngredientEntity(
                    ingredients.get(i).quantity,
                    ingredients.get(i).measure,
                    ingredients.get(i).ingredient,
                    recipeEntity
            );
            ingredientEntity.save();
        }
    }
}
