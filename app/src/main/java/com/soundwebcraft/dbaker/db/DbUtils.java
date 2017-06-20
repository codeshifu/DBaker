package com.soundwebcraft.dbaker.db;

import com.soundwebcraft.dbaker.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.soundwebcraft.dbaker.db.RecipeEntity.IngredientEntity;
import static com.soundwebcraft.dbaker.db.RecipeEntity.StepEntity;

public class DbUtils {
    public static void saveStepEntities(List<Recipe.Steps> steps, RecipeEntity recipeEntity) {
        for (int i = 0; i < steps.size(); i++) {
            StepEntity stepEntity = new StepEntity(
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
            IngredientEntity ingredientEntity = new IngredientEntity(
                    ingredients.get(i).quantity,
                    ingredients.get(i).measure,
                    ingredients.get(i).ingredient,
                    recipeEntity
            );
            ingredientEntity.save();
        }
    }

    public static Recipe convertToRecipeObject(RecipeEntity recipeEntity) {
        List<RecipeEntity.StepEntity> se = new ArrayList<>();
        List<RecipeEntity.IngredientEntity> ie = new ArrayList<>();
        String whereClause = "recipe = ?";
        ie = IngredientEntity.find(IngredientEntity.class, whereClause, String.valueOf(recipeEntity.getId()));
        se = StepEntity.find(StepEntity.class, whereClause, String.valueOf(recipeEntity.getId()));

        List<Recipe.Ingredients> ingredients = extractIngredients(ie);
        List<Recipe.Steps> steps = extractSteps(se);

        return new Recipe(
                recipeEntity.getRecipeId(),
                recipeEntity.getName(),
                ingredients, steps, recipeEntity.getServings(), recipeEntity.getImage()
        );
    }

    public static List<Recipe.Steps> extractSteps(List<StepEntity> se) {
        List<Recipe.Steps> steps = new ArrayList<>();
        for (StepEntity entity : se) {
            steps.add(new Recipe.Steps(
                    entity.getStepId(),
                    entity.getShortdescription(),
                    entity.getDescription(),
                    entity.getVideourl(),
                    entity.getThumbnailUrl()
            ));
        }
        return steps;
    }

    public static List<Recipe.Ingredients> extractIngredients(List<RecipeEntity.IngredientEntity> ie) {
        List<Recipe.Ingredients> ingredients = new ArrayList<>();
        for (IngredientEntity entity : ie) {
            ingredients.add(new Recipe.Ingredients(entity.getQuantity(), entity.getMeasure(), entity.getIngredient()
            ));
        }
        return ingredients;
    }

    public static List<Recipe.Ingredients> getIngredientListForDesiredRecipe(String id) {
        RecipeEntity recipeEntity = RecipeEntity.findById(RecipeEntity.class, Long.parseLong(id));
        String whereClause = "recipe = ?";
        return extractIngredients(IngredientEntity.find(IngredientEntity.class, whereClause, id));
    }
}
