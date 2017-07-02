package com.soundwebcraft.dbaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.adapater.RecipeAdapter;
import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.data.remote.RecipeService;
import com.soundwebcraft.dbaker.data.remote.RetrofitClient;
import com.soundwebcraft.dbaker.data.db.DbUtils;
import com.soundwebcraft.dbaker.data.db.RecipeEntity;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;
import com.soundwebcraft.dbaker.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.recyclerview)
    EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.empty_state_feedback)
    TextView emptyStateFeedback;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;

    private RecipeAdapter mAdapter;
    private Context mContext;
    private RecipeService mService;

    private Unbinder unbinder;
    public static final String TAG = RecipeListFragment.class.getSimpleName();
    private List<Recipe> mRecipes = new ArrayList<>();

    // material design guidelines - progress activity
    private boolean isLoading = true;

    public OnListItemSelectedListener listener;

    public interface OnListItemSelectedListener {
        public void onItemSelected(Recipe recipe);
    }

    public RecipeListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement interface");
        }
        mContext = context;
        mService = RetrofitClient.getClient().create(RecipeService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        unbinder = ButterKnife.bind(this, v);

        mAdapter = new RecipeAdapter(this, mContext, mRecipes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        long count = RecipeEntity.count(RecipeEntity.class, null, null);
        if (count > 0) {
            // hide progress bar
            toggleLoadingIndicator(false);
            // load data from db
            List<RecipeEntity> recipeEntities = RecipeEntity.listAll(RecipeEntity.class);
            for (RecipeEntity recipeEntity : recipeEntities) {
                Recipe recipe = DbUtils.convertToRecipeObject(recipeEntity);
                mRecipes.add(recipe);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if (NetworkUtils.isConnected(mContext)) {
                fetchRecipes();
            } else {
                showEmptyViewStateWithoutLoadingIndicator(getString(R.string.no_internet));
            }
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // toggle loading indicator on/off
    private void toggleLoadingIndicator(boolean state) {
        if (state) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            isLoading = true;
        } else {
            isLoading = false;
            mLoadingIndicator.setVisibility(View.GONE);
        }
    }

    // display empty state
    private void showEmptyView(String msg) {
        emptyView.setVisibility(View.VISIBLE);
        if (msg != null) emptyStateFeedback.setText(msg);
        mRecyclerView.setEmptyView(emptyView);
    }

    // fetch recipes
    void fetchRecipes() {
        mService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    toggleLoadingIndicator(false);
                    if (response.body().size() > 0) {
                        List<Recipe> results = response.body();
                        parseJsonResponse(results);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        showEmptyViewStateWithoutLoadingIndicator(getString(R.string.response_no_data));
                    }
                } else {
                    showEmptyViewStateWithoutLoadingIndicator(getString(R.string.response_fail));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
                showEmptyViewStateWithoutLoadingIndicator(getString(R.string.response_error_fetching));
                Log.e(TAG, getString(R.string.response_error) + t.getMessage());
            }
        });
    }

    private void showEmptyViewStateWithoutLoadingIndicator(String msg) {
        toggleLoadingIndicator(false);
        showEmptyView(msg);
    }

    private void parseJsonResponse(List<Recipe> results) {
        int count = 1;
        for (Recipe recipe : results) {
            mRecipes.add(recipe);
            // save entities
            List<Recipe.Ingredients> ingredients = recipe.ingredients;
            List<Recipe.Steps> steps = recipe.steps;
            final int id = recipe.id;
            RecipeEntity recipeEntity = new RecipeEntity(id, recipe.name, recipe.servings, recipe.image);
            recipeEntity.save();

            if (count == 1)
                saveDefaultDesiredRecipe(String.valueOf(recipeEntity.getId()));

            DbUtils.saveIngredientEntities(ingredients, recipeEntity);
            DbUtils.saveStepEntities(steps, recipeEntity);

            count++;
        }
    }

    private void saveDefaultDesiredRecipe(String id) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(getString(R.string.pref_name), 0).edit();
        editor.putString(getString(R.string.pref_key), id);
        editor.apply();
    }
}
