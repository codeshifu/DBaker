package com.soundwebcraft.dbaker.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.data.db.DbUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private final Intent intent;

    List<String> mCollection = new ArrayList<>();

    private void initData() {
        mCollection.clear();
        Context context = mContext.getApplicationContext();
        String string = mContext.getSharedPreferences(context.getString(R.string.pref_name), 0).getString(
                context.getString(R.string.pref_key), context.getString(R.string.pref_default));

        List<Recipe.Ingredients> ingredients = DbUtils.getIngredientListForDesiredRecipe(string);

        for (Recipe.Ingredients ingredient :
                ingredients) {
            mCollection.add(ingredient.toString());
        }
    }

    public WidgetDataProvider(Context context, Intent intent) {
        this.mContext = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        view.setTextViewText(R.id.widget_textview, mCollection.get(position));

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
