package com.soundwebcraft.dbaker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.soundwebcraft.dbaker.utils.DbUtils;
import com.soundwebcraft.dbaker.data.db.RecipeContract;
import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.fragments.RecipeListFragment;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {
    private static final String TAG = RecipeListFragment.class.getSimpleName();
    private Context mContext;
    private List<Recipe> mData;

    public RecipeLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        mData = data;
        super.deliverResult(data);
    }

    @Override
    public List<Recipe> loadInBackground() {
        List<Recipe> recipes = null;
        Cursor cursor = DbUtils.contentResolver(mContext).query(
                RecipeContract.RecipeEntry.CONTENT_URI,
                null, null, null, null
        );
        if (cursor != null)
            if (cursor.getCount() > 0) {
                cursor.close();
                recipes = DbUtils.queryRecipes(mContext);
            }
        return recipes;
    }
}
