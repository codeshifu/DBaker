package com.soundwebcraft.dbaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.RecipeDetailActivity;
import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.data.remote.RecipeService;
import com.soundwebcraft.dbaker.data.remote.RetrofitClient;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;

import org.parceler.Parcels;

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

    private RecipeAdapter mAdapter;
    private Context mContext;
    private RecipeService mService;

    private Unbinder unbinder;
    public static final String TAG = RecipeListFragment.class.getSimpleName();
    private List<Recipe> mRecipes = new ArrayList<>();

    public RecipeListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mService = RetrofitClient.getClient().create(RecipeService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        unbinder = ButterKnife.bind(this, v);

        mAdapter = new RecipeAdapter(mContext, mRecipes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        if (isConnected()) {
            fetchRecipes();
        } else {
            showEmptyView(null);
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showEmptyView(String msg) {
        if (msg != null) {
            emptyStateFeedback.setText(msg);
        }
        mRecyclerView.setEmptyView(emptyView);
    }

    void fetchRecipes() {
        mService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        Log.d(TAG, getString(R.string.response_success) + response.body().size());
                        List<Recipe> results = response.body();
                        for (Recipe recipe : results) {
                            mRecipes.add(recipe);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        showEmptyView(getString(R.string.response_no_data));
                    }
                } else Log.d(TAG, getString(R.string.response_fail) + code);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.e(TAG, getString(R.string.response_error) + t.getMessage());
            }
        });
    }

    class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

        private Context mContext;
        private List<Recipe> mRecipes;

        RecipeAdapter(Context context, List<Recipe> recipes) {
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

                    ivRecipe.setImageResource(recipe.getImageResourceId());
                    tvRecipeTitle.setText(recipe.getName());
                    tvRecipeGuide.setText(step);
                    tvRecipeSteps.setText(getString(R.string.steps_count, stepsCount));
                }
            }

            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                Recipe recipe = mRecipes.get(clickedPosition);
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, Parcels.wrap(recipe));
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
