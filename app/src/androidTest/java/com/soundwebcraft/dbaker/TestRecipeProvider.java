package com.soundwebcraft.dbaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import com.soundwebcraft.dbaker.data.db.RecipeDbHelper;
import com.soundwebcraft.dbaker.data.db.RecipeProvider;

import org.junit.runner.RunWith;

import static com.soundwebcraft.dbaker.data.db.RecipeContract.CONTENT_AUTHORITY;
import static com.soundwebcraft.dbaker.data.db.RecipeContract.RecipeEntry;

@RunWith(AndroidJUnit4.class)
public class TestRecipeProvider extends ProviderTestCase2<RecipeProvider> {
    private Context mContext = InstrumentationRegistry.getTargetContext();

    public TestRecipeProvider() {
        super(RecipeProvider.class, CONTENT_AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    public void testInsertAndRead() {
        deleteDB();
        // recipe values
        int recipe_id = 1;
        String recipe_name = "Brownies";

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeEntry._ID, recipe_id);
        recipeValues.put(RecipeEntry.COLUMN_NAME, recipe_name);
        recipeValues.put(RecipeEntry.COLUMN_SERVINGS, 2);
        recipeValues.put(RecipeEntry.COLUMN_IMAGE, "brownies.jpg");

        String[] columns = {
                RecipeEntry._ID,
                RecipeEntry.COLUMN_NAME,
                RecipeEntry.COLUMN_IMAGE,
                RecipeEntry.COLUMN_SERVINGS
        };

        Uri uri = mContext.getContentResolver().insert(RecipeEntry.CONTENT_URI, recipeValues);
        assert uri != null;
        assertEquals(1, Integer.parseInt(uri.getLastPathSegment()));

        Cursor cursor = mContext.getContentResolver().query(RecipeEntry.CONTENT_URI,
                columns, null, null, null, null
        );

        int recipeId = 0;
        String name = null;
        String image = null;

        assert cursor != null;
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(RecipeEntry._ID);
            recipeId = cursor.getInt(id);

            int nameIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_NAME);
            name = cursor.getString(nameIndex);

            int imageIndex = cursor.getColumnIndex(RecipeEntry.COLUMN_IMAGE);
            image = cursor.getString(imageIndex);
        }

        assertEquals(recipe_id, recipeId);
        assertEquals(recipe_name, name);

        cursor.close();
    }

    private void deleteDB() {
        mContext.deleteDatabase(RecipeDbHelper.DATABASE_NAME);
    }
}
