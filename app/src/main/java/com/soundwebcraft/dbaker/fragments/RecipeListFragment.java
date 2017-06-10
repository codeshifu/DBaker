package com.soundwebcraft.dbaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.soundwebcraft.dbaker.RecipeDetail;
import com.soundwebcraft.dbaker.dummy.Recipe;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.recipe_recycler_view) EmptyStateRecyclerView mRecyclerView;
    @BindView(R.id.recipe_empty_view)  View emptyView;
    @BindView(R.id.empty_state_feedback) TextView emptyStateFeedback;

    private RecipeAdapter mAdapter;
    private Context mContext;

    private Unbinder unbinder;

    public RecipeListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        unbinder = ButterKnife.bind(this, v);

        mAdapter = new RecipeAdapter(mContext, Recipe.getRecipes());

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        // set recyclerview empty state
        if (isConnected()) {
            // TODO - deal with response from server
        } else {
            // if internet not available
            // display recyclerview empty state
            mRecyclerView.setEmptyView(emptyView);
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

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

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.recipe_image) ImageView ivRecipe;
            @BindView(R.id.recipe_title) TextView tvRecipeTitle;
            @BindView(R.id.recipe_steps) TextView tvRecipeSteps;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            void bind(Recipe recipe) {
                ivRecipe.setImageResource(recipe.getResourceId());
                tvRecipeTitle.setText(recipe.getName());
                tvRecipeSteps.setText(getString(R.string.steps_count, recipe.getStep()));
            }

            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                Recipe recipe = mRecipes.get(clickedPosition);
                Intent intent = new Intent(mContext, RecipeDetail.class);
                intent.putExtra(RecipeDetail.EXTRA_RECIPE, recipe);
                startActivity(intent);
            }
        }
    }

    // check internet connection state
    boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
