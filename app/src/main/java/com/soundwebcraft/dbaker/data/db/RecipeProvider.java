package com.soundwebcraft.dbaker.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.soundwebcraft.dbaker.data.db.RecipeContract.CONTENT_AUTHORITY;
import static com.soundwebcraft.dbaker.data.db.RecipeContract.IngredientEntry;
import static com.soundwebcraft.dbaker.data.db.RecipeContract.RecipeEntry;
import static com.soundwebcraft.dbaker.data.db.RecipeContract.StepEntry;


public class RecipeProvider extends ContentProvider {
    private RecipeDbHelper mDbHelper;
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // prepare the possible uri's before taking any action in this provider
    static {
        // content://com.soundwebcraft.dbaker/recipes
        mUriMatcher.addURI(CONTENT_AUTHORITY, RecipeEntry.PATH_RECIPE, RecipeEntry.RECIPE);
        // content://com.soundwebcraft.dbaker/recipes/2
        mUriMatcher.addURI(CONTENT_AUTHORITY, RecipeEntry.PATH_RECIPE + "/#", RecipeEntry.RECIPE_ID);
        // content://com.soundwebcraft.dbaker/steps
        mUriMatcher.addURI(CONTENT_AUTHORITY, StepEntry.PATH_STEP, StepEntry.STEP);
        // content://com.soundwebcraft.dbaker/ingredients
        mUriMatcher.addURI(CONTENT_AUTHORITY, IngredientEntry.PATH_INGREDIENT, IngredientEntry.INGEREDIENT);
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();
        selection = RecipeEntry._ID + " = ?";

        switch (mUriMatcher.match(uri)) {
            case RecipeEntry.RECIPE:
                cursor = db.query(
                        RecipeEntry.TABLE_NAME,
                        null, null, null, null, null, null
                );
                break;
            case RecipeEntry.RECIPE_ID:
                cursor = db.query(
                        RecipeEntry.TABLE_NAME,
                        null, selection, selectionArgs, null, null, null
                );
                break;
            case StepEntry.STEP:
                selection = "recipe_id = ?";
                cursor = db.query(
                        StepEntry.TABLE_NAME,
                        null, selection, selectionArgs, null, null, null
                );
                break;
            case IngredientEntry.INGEREDIENT:
                selection = "recipe_id = ?";
                cursor = db.query(
                        IngredientEntry.TABLE_NAME,
                        null, selection, selectionArgs, null, null, null
                );
                break;
            default:
                throw new UnsupportedOperationException("Invalid uri: " + uri.toString());
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case RecipeEntry.RECIPE:
                return RecipeEntry.CONTENT_TYPE;
            case RecipeEntry.RECIPE_ID:
                return RecipeEntry.CONTENT_ITEM_TYPE;
            case StepEntry.STEP:
                return StepEntry.CONTENT_TYPE;
            case IngredientEntry.INGEREDIENT:
                return IngredientEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Invalid uri: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id;
        SQLiteDatabase db = getWriteableDatabase();

        if (values == null) throw new IllegalArgumentException("Content values cannot be null");

        switch (mUriMatcher.match(uri)) {
            case RecipeEntry.RECIPE:
                String recipeName = values.getAsString(RecipeEntry.COLUMN_NAME);
                if (recipeName.isEmpty()) throw new IllegalArgumentException("recipe name cannot be empty.");
                id = db.insertOrThrow(
                        RecipeEntry.TABLE_NAME,
                        null,
                        values
                );
                return ContentUris.withAppendedId(RecipeEntry.CONTENT_URI, id);
            case StepEntry.STEP:
                id = db.insert(
                        StepEntry.TABLE_NAME,
                        null,
                        values
                );

                return ContentUris.withAppendedId(StepEntry.CONTENT_URI, id);
            case IngredientEntry.INGEREDIENT:
                id = db.insert(
                        IngredientEntry.TABLE_NAME,
                        null,
                        values
                );
                return ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id);
            default:
                throw new UnsupportedOperationException("Invalid uri: " + uri.toString());
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // this method has been intentionally omitted
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // this method has been intentionally omitted
        return 0;
    }

    private SQLiteDatabase getReadableDatabase() {
        return mDbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWriteableDatabase() {
        return mDbHelper.getWritableDatabase();
    }
}
