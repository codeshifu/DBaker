package com.soundwebcraft.dbaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.data.model.Recipe.Ingredients;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class IngredientListFragment extends Fragment {

    private static final String INGREDIENT_EXTRA = "ingredients";
    public static final String TAG = IngredientListFragment.class.getSimpleName();
    @BindView(R.id.recyclerview)
    EmptyStateRecyclerView mRecyclerview;
    @BindView(R.id.empty_state_feedback)
    TextView mEmptyStateFeedback;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    private Unbinder unbinder;
    private Context mContext;
    private List<Ingredients> mIngredientsList;

    public IngredientListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        unbinder = ButterKnife.bind(this, v);

        mIngredientsList = Parcels.unwrap(getArguments().getParcelable(INGREDIENT_EXTRA));

        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setNestedScrollingEnabled(false);
        mRecyclerview.setAdapter(new IngredientAdapter(mContext, mIngredientsList));


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static IngredientListFragment newInstance(List<Ingredients> ingredients) {

        Bundle args = new Bundle();
        args.putParcelable(INGREDIENT_EXTRA, Parcels.wrap(ingredients));
        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
        private Context adapterContext;
        private List<Ingredients> adapterIngredientLists;

        IngredientAdapter(Context context, List<Ingredients> ingredients) {
            adapterContext = context;
            adapterIngredientLists = ingredients;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(adapterContext);
            View v = inflater.inflate(R.layout.list_item_ingredient, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Ingredients ingredient = mIngredientsList.get(position);
            holder.bind(ingredient);
        }

        @Override
        public int getItemCount() {
            return adapterIngredientLists.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ingredient)
            TextView tvIngredient;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bind(Ingredients ingredient) {
                String ingr = ingredient.getIngredient();
                ingr = Character.toUpperCase(ingr.charAt(0)) + ingr.substring(1);
                String measure = ingredient.getMeasure();
                double qty = ingredient.getQuantity();

                DecimalFormat df = new DecimalFormat("##.#");
                String s = ingr + ", " + df.format(qty) + " " + measure;
                tvIngredient.setText(s);
            }
        }
    }
}
