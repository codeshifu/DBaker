package com.soundwebcraft.dbaker.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.soundwebcraft.dbaker.data.db.RecipeContract.*;


public class RecipeDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "baker.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecipeEntry.createTable());
        db.execSQL(StepEntry.createTable());
        db.execSQL(IngredientEntry.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);

        // create fresh database tables
        onCreate(db);
    }
}
