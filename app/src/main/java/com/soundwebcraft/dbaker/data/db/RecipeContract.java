package com.soundwebcraft.dbaker.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.soundwebcraft.dbaker";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + CONTENT_AUTHORITY);

    public RecipeContract() {
    }

    public static abstract class RecipeEntry implements BaseColumns {

        public static final String TABLE_NAME = "recipes";
        public static final String PATH_RECIPE = TABLE_NAME;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();

        // supported paths
        public static final int RECIPE = 10;
        public static final int RECIPE_ID = 11;

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // columns
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";

        public static String createTable() {
            return "CREATE TABLE " + RecipeEntry.TABLE_NAME + "(" +
                    RecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                    RecipeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    RecipeEntry.COLUMN_IMAGE + " TEXT, " +
                    RecipeEntry.COLUMN_SERVINGS + " INTEGER" +
                    ");";
        }
    }

    public static abstract class StepEntry implements BaseColumns {

        public static final String TABLE_NAME = "steps";
        public static final String PATH_STEP = TABLE_NAME;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEP).build();

        // supported paths
        public static final int STEP = 20;
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // columns
        public static final String COLUMN_RECIPE_KEY = "recipe_id";
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "video_url";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";

        // helper method to create step table
        public static String createTable() {
            return "CREATE TABLE " + StepEntry.TABLE_NAME + "(" +
                    StepEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    StepEntry.COLUMN_RECIPE_KEY + " INTEGER NOT NULL, " +
                    StepEntry.COLUMN_STEP_ID + " INTEGER, " +
                    StepEntry.COLUMN_SHORT_DESCRIPTION + " TEXT, " +
                    StepEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    StepEntry.COLUMN_THUMBNAIL_URL + " TEXT, " +
                    StepEntry.COLUMN_VIDEO_URL + " TEXT, " +

                    "FOREIGN KEY (" + StepEntry.COLUMN_RECIPE_KEY + ") REFERENCES " +
                    RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + ")" +
                    ");";
        }
    }

    public static abstract class IngredientEntry implements BaseColumns {

        public static final String TABLE_NAME = "ingredients";
        public static final String PATH_INGREDIENT = TABLE_NAME;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        // supported paths
        public static final int INGEREDIENT = 30;
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // columns
        public static final String COLUMN_RECIPE_KEY = "recipe_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";

        public static String createTable() {
            return "CREATE TABLE " + IngredientEntry.TABLE_NAME + "(" +
                    IngredientEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    IngredientEntry.COLUMN_RECIPE_KEY + " INTEGER NOT NULL, " +
                    IngredientEntry.COLUMN_INGREDIENT + " TEXT, " +
                    IngredientEntry.COLUMN_MEASURE + " TEXT, " +
                    IngredientEntry.COLUMN_QUANTITY + " REAL, " +

                    "FOREIGN KEY (" + IngredientEntry.COLUMN_RECIPE_KEY + ") REFERENCES " +
                    RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + ")" +
                    ");";
        }
    }
}
