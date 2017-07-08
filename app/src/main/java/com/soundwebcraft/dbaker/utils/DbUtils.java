package com.soundwebcraft.dbaker.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.soundwebcraft.dbaker.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.soundwebcraft.dbaker.data.db.RecipeContract.IngredientEntry;
import static com.soundwebcraft.dbaker.data.db.RecipeContract.RecipeEntry;
import static com.soundwebcraft.dbaker.data.db.RecipeContract.StepEntry;

public class DbUtils {
    public static List<Recipe> queryRecipes(Context context) {
        Cursor cursor = contentResolver(context).query(
                RecipeEntry.CONTENT_URI,
                null, null, null, null
        );

        return convertCursorToRecipeObject(context, cursor);
    }


    public static List<Recipe.Steps> getRecipeSteps(Context context, int id) {
        List<Recipe.Steps> steps = new ArrayList<>();
        Cursor cursor = contentResolver(context).query(
                StepEntry.CONTENT_URI,
                null,
                null,
                new String[]{String.valueOf(id)},
                null
        );

        steps = convertCursorToStepObject(context, cursor);
        cursor.close();

        return steps;
    }
    public static List<Recipe.Ingredients> getRecipeIngredients(Context context, int id) {
        List<Recipe.Ingredients> ingredients = new ArrayList<>();
        Cursor cursor = contentResolver(context).query(
                IngredientEntry.CONTENT_URI,
                null,
                null,
                new String[]{String.valueOf(id)},
                null
        );

        ingredients = convertCursorToIngredientObject(context, cursor);
        cursor.close();

        return ingredients;
    }

    public static List<Recipe> convertCursorToRecipeObject(Context context, Cursor cursor) {
        if (cursor == null) throw new NullPointerException();
        List<Recipe> recipes = new ArrayList<>();
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(RecipeEntry._ID);
            int id = cursor.getInt(idIndex);

            List<Recipe.Ingredients> ingredients = getRecipeIngredients(context, id);
            List<Recipe.Steps> steps = getRecipeSteps(context, id);

            int nameIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_NAME);
            String name = cursor.getString(nameIndex);

            int imageIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_IMAGE);
            String image = cursor.getString(imageIndex);

            int servingsIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_SERVINGS);
            int servings = cursor.getInt(servingsIndex);

            recipes.add(new Recipe(
                    id,
                    name,
                    ingredients,
                    steps,
                    servings,
                    image)
            );
        }
        return recipes;
    }
    public static List<Recipe.Steps> convertCursorToStepObject(Context context, Cursor cursor) {
        if (cursor == null) throw new NullPointerException();
        List<Recipe.Steps> steps = new ArrayList<>();
        while (cursor.moveToNext()) {

            int step_index = cursor.getColumnIndex(StepEntry.COLUMN_STEP_ID);
            int stepId = cursor.getInt(step_index);

            int descIndex = cursor.getColumnIndex(StepEntry.COLUMN_DESCRIPTION);
            String desc = cursor.getString(descIndex);

            int short_desc_index = cursor.getColumnIndex(StepEntry.COLUMN_SHORT_DESCRIPTION);
            String short_desc = cursor.getString(short_desc_index);

            int thumbnail_index = cursor.getColumnIndex(StepEntry.COLUMN_THUMBNAIL_URL);
            String thumbnail = cursor.getString(thumbnail_index);

            int vid_url_index = cursor.getColumnIndex(StepEntry.COLUMN_VIDEO_URL);
            String vid_url = cursor.getString(vid_url_index);


            steps.add(new Recipe.Steps(
                            stepId,
                            short_desc,
                            desc,
                            vid_url,
                            thumbnail
                    )
            );
        }
        return steps;
    }
    public static List<Recipe.Ingredients> convertCursorToIngredientObject(Context context, Cursor cursor) {
        if (cursor == null) throw new NullPointerException();
        List<Recipe.Ingredients> ingredients = new ArrayList<>();
        while (cursor.moveToNext()) {
            int ingredientIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT);
            String ingredient = cursor.getString(ingredientIndex);

            int measureIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_MEASURE);
            String measure = cursor.getString(measureIndex);

            int quantityIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_QUANTITY);
            int quantity = cursor.getInt(quantityIndex);

            ingredients.add(new Recipe.Ingredients(quantity, measure, ingredient));
        }
        return ingredients;
    }

    public static void persistToDb(Context context, List<Recipe> recipes) {
        int count = 0;

        insertRecipes(context, recipes);

        for (Recipe recipe : recipes) {
            final int recipeId = recipe.getId();

            List<Recipe.Ingredients> ingredients = recipe.ingredients;
            List<Recipe.Steps> steps = recipe.steps;

            insertRecipeSteps(context, recipeId, steps);
            insertRecipeIngredients(context, recipeId, ingredients);
            count++;
        }
    }

    public static int insertRecipeIngredients(Context context, int recipeId, List<Recipe.Ingredients> ingredients) {
        int size = ingredients.size();
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            ContentValues value = new ContentValues();
            value.put(IngredientEntry.COLUMN_RECIPE_KEY, recipeId);
            value.put(IngredientEntry.COLUMN_INGREDIENT, ingredients.get(i).getIngredient());
            value.put(IngredientEntry.COLUMN_QUANTITY, ingredients.get(i).getQuantity());
            value.put(IngredientEntry.COLUMN_MEASURE, ingredients.get(i).getMeasure());

            values[i] = value;
        }
        return contentResolver(context).bulkInsert(
                IngredientEntry.CONTENT_URI,
                values
        );
    }

    public static int insertRecipeSteps(Context context, int recipeId, List<Recipe.Steps> steps) {
        int size = steps.size();
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            ContentValues value = new ContentValues();
            value.put(StepEntry.COLUMN_RECIPE_KEY, recipeId);
            value.put(StepEntry.COLUMN_STEP_ID, steps.get(i).getId());
            value.put(StepEntry.COLUMN_DESCRIPTION, steps.get(i).getDescription());
            value.put(StepEntry.COLUMN_SHORT_DESCRIPTION, steps.get(i).getShortdescription());
            value.put(StepEntry.COLUMN_THUMBNAIL_URL, steps.get(i).getThumbnailurl());
            value.put(StepEntry.COLUMN_VIDEO_URL, steps.get(i).getVideourl());

            values[i] = value;
        }
        return contentResolver(context).bulkInsert(
                StepEntry.CONTENT_URI,
                values
        );
    }

    public static int insertRecipes(Context context, List<Recipe> recipes) {
        int size = recipes.size();
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            ContentValues value = new ContentValues();
            value.put(RecipeEntry._ID, recipes.get(i).getId());
            value.put(RecipeEntry.COLUMN_NAME, recipes.get(i).getName());
            value.put(RecipeEntry.COLUMN_IMAGE, recipes.get(i).getImage());
            value.put(RecipeEntry.COLUMN_SERVINGS, recipes.get(i).getServings());

            values[i] = value;
        }
        return contentResolver(context).bulkInsert(
                RecipeEntry.CONTENT_URI,
                values
        );
    }

    public static ContentResolver contentResolver(Context context) {
        return context.getContentResolver();
    }

    public static List<Recipe.Ingredients> getIngredientListForDesiredRecipe(Context context, String id) {
        return getRecipeIngredients(context, Integer.parseInt(id));
    }
}
