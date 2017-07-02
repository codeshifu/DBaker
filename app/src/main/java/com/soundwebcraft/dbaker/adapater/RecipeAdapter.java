package com.soundwebcraft.dbaker.adapater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.fragments.RecipeListFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private RecipeListFragment mRecipeListFragment;
    private Context mContext;
    private List<Recipe> mRecipes;

    public RecipeAdapter(RecipeListFragment recipeListFragment, Context context, List<Recipe> recipes) {
        mRecipeListFragment = recipeListFragment;
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
        @BindView(R.id.recipe_image)
        ImageView ivRecipe;
        @BindView(R.id.recipe_title)
        TextView tvRecipeTitle;
        @BindView(R.id.recipe_steps)
        TextView tvRecipeSteps;
        @BindView(R.id.recipe_guide)
        TextView tvRecipeGuide;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            if (recipe != null) {
                String step = null;
                try {
                    step = recipe.getSteps().get((int) (Math.random() * 4 + 1) + 1).getDescription();
                } catch (IndexOutOfBoundsException e) {
                    step = "";
                    e.printStackTrace();
                }
                int stepsCount = recipe.getSteps().size();

                if (!TextUtils.isEmpty(recipe.getImage())) {
                    Picasso.with(mContext)
                            .load(recipe.getImage())
                            .error(R.drawable.no_preview)
                            .into(ivRecipe);
                } else {
                    ivRecipe.setImageResource(recipe.getImageResourceId());
                }

                tvRecipeTitle.setText(recipe.getName());
                tvRecipeGuide.setText(step);
                tvRecipeSteps.setText(mRecipeListFragment.getString(R.string.steps_count, stepsCount));
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Recipe recipe = mRecipes.get(clickedPosition);
            mRecipeListFragment.listener.onItemSelected(recipe);
        }
    }
}
