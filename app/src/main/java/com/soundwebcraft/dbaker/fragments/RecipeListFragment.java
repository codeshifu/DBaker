package com.soundwebcraft.dbaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.dummy.Recipe;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;

import java.util.List;

public class RecipeListFragment extends Fragment {

    private EmptyStateRecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private Context mContext;

    public RecipeListFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mAdapter = new RecipeAdapter(mContext, Recipe.getRecipes());

        mRecyclerView = (EmptyStateRecyclerView) v.findViewById(R.id.recipe_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        return v;
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

        private Context mContext;
        private List<Recipe> mRecipes;

        public RecipeAdapter(Context context, List<Recipe> recipes) {
            mContext = context;
            mRecipes = recipes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_recipe, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Recipe recipe = mRecipes.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivRecipe;
            TextView tvRecipeTitle;
            TextView tvRecipeSteps;

            ViewHolder(View itemView) {
                super(itemView);
                ivRecipe = (ImageView) itemView.findViewById(R.id.recipe_image);
                tvRecipeTitle = (TextView) itemView.findViewById(R.id.recipe_title);
                tvRecipeSteps = (TextView) itemView.findViewById(R.id.recipe_steps);
            }

            void bind(Recipe recipe) {
                ivRecipe.setImageResource(recipe.getResourceId());
                tvRecipeTitle.setText(recipe.getName());
                tvRecipeSteps.setText(getString(R.string.steps_count, recipe.getStep()));
            }
        }
    }
}
