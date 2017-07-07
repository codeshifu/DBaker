package com.soundwebcraft.dbaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.soundwebcraft.dbaker.data.db.RecipeDbHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.soundwebcraft.dbaker.data.db.RecipeContract.IngredientEntry;
import static com.soundwebcraft.dbaker.data.db.RecipeContract.RecipeEntry;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestSQLite {

    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void testCreateDb() {
        deleteDB();
        SQLiteDatabase db = getDatabase();
        assertEquals(true, db.isOpen());
        deleteDB();
    }

    @Test
    public void testInsertWithoutConstraint() {
        double qty = 2.2;
        String measure = "Table Spoon";
        String ingredient = "Graham. Cherry";

        SQLiteDatabase db = getDatabase();

        ContentValues values = new ContentValues();
        values.put(IngredientEntry.COLUMN_QUANTITY, qty);
        values.put(IngredientEntry.COLUMN_MEASURE, measure);
        values.put(IngredientEntry.COLUMN_INGREDIENT, ingredient);

        long id = db.insert(IngredientEntry.TABLE_NAME, null, values);

        assertEquals(-1L, id);
        deleteDB();
    }

    @Test
    public void testInsert() {
        deleteDB();
        SQLiteDatabase db = getDatabase();

        // recipe values
        int recipe_id = 1;
        String recipe_name = "Brownies";

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeEntry._ID, recipe_id);
        recipeValues.put(RecipeEntry.COLUMN_NAME, recipe_name);

        long recipeInsertedId = db.insert(RecipeEntry.TABLE_NAME, null, recipeValues);
        assertEquals(1, recipeInsertedId);

        //

        double qty = 2.2;
        String measure = "Table Spoon";
        String ingredient = "Graham. Cherry";

        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put(IngredientEntry.COLUMN_RECIPE_KEY, recipeInsertedId);
        ingredientValues.put(IngredientEntry.COLUMN_QUANTITY, qty);
        ingredientValues.put(IngredientEntry.COLUMN_MEASURE, measure);
        ingredientValues.put(IngredientEntry.COLUMN_INGREDIENT, ingredient);

        long ingredientInsertedId = db.insert(IngredientEntry.TABLE_NAME, null, ingredientValues);

        assertEquals(1, ingredientInsertedId);
        deleteDB();
    }


    @Test
    public void testInsertAndQuery() {
        SQLiteDatabase db = getDatabase();

        // recipe values
        int recipe_id = 1;
        String recipe_name = "Brownies";
        String recipe_image = "brownies.png";

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeEntry._ID, recipe_id);
        recipeValues.put(RecipeEntry.COLUMN_NAME, recipe_name);
        recipeValues.put(RecipeEntry.COLUMN_IMAGE, recipe_image);

        long recipeInsertedId = db.insert(RecipeEntry.TABLE_NAME, null, recipeValues);
        assertEquals(1, recipeInsertedId);

        String[] columns = {
                RecipeEntry._ID,
                RecipeEntry.COLUMN_NAME,
                RecipeEntry.COLUMN_IMAGE,
                RecipeEntry.COLUMN_SERVINGS
        };


        Cursor cursor = db.query(RecipeEntry.TABLE_NAME,
                columns, null, null, null, null, null
        );

        int recipeId = 0;
        String name = null;
        String image = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(RecipeEntry._ID);
            recipeId = cursor.getInt(id);

            int nameIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_NAME);
            name = cursor.getString(nameIndex);

            int imageIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_IMAGE);
            image = cursor.getString(imageIndex);
        }
        cursor.close();

        assertEquals(recipe_id, recipeId);
        assertEquals(recipe_name, name);
        assertEquals(recipe_image, image);
        deleteDB();
    }

    @Test
    public void testSimpleBulkInsert() {
        deleteDB();
        SQLiteDatabase db = getDatabase();

        // recipe values
        int recipe_id = 1;
        String recipe_name = "Brownies";
        String recipe_image = "brownies.png";

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeEntry._ID, recipe_id);
        recipeValues.put(RecipeEntry.COLUMN_NAME, recipe_name);
        recipeValues.put(RecipeEntry.COLUMN_IMAGE, recipe_image);


        db.beginTransaction();
        long recipeInsertedId = -1;
        try {
            recipeInsertedId = db.insert(RecipeEntry.TABLE_NAME, null, recipeValues);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        assertEquals(1L, recipeInsertedId);
        deleteDB();
    }

    private SQLiteDatabase getDatabase() {
        return new RecipeDbHelper(mContext).getWritableDatabase();
    }

    private void deleteDB() {
        mContext.deleteDatabase(RecipeDbHelper.DATABASE_NAME);
    }
}
