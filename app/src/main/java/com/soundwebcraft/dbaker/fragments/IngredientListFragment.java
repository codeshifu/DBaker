package com.soundwebcraft.dbaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.vipulasri.timelineview.TimelineView;
import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;
import com.soundwebcraft.dbaker.utils.VectorDrawableUtils;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class IngredientListFragment extends Fragment {

    private static final String INGREDIENT_EXTRA = "ingredients";
    @BindView(R.id.recyclerview)
    EmptyStateRecyclerView mRecyclerview;
    @BindView(R.id.empty_state_feedback)
    TextView mEmptyStateFeedback;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    private Unbinder unbinder;
    private Context mContext;
    private List<Recipe.Ingredients> mIngredientsList;

    public IngredientListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        unbinder = ButterKnife.bind(this, v);

        mIngredientsList = Parcels.unwrap(getArguments().getParcelable(INGREDIENT_EXTRA));
        Toast.makeText(mContext, "" + mIngredientsList.size(), Toast.LENGTH_SHORT).show();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setNestedScrollingEnabled(false);
        mRecyclerview.setAdapter(new IngredientAdapter(mContext));

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static IngredientListFragment newInstance(List<Recipe.Ingredients> ingredients) {

        Bundle args = new Bundle();
        args.putParcelable(INGREDIENT_EXTRA, Parcels.wrap(ingredients));
        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
        private Context adapterContext;

        public IngredientAdapter(Context context) {
            adapterContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(adapterContext);
            View v = inflater.inflate(R.layout.list_item_ingredient, parent, false);

            return new ViewHolder(v, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemViewType(int position) {
            return TimelineView.getTimeLineViewType(position, getItemCount());
        }

        @Override
        public int getItemCount() {
            return 15;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.time_marker)
            TimelineView mTimelineView;


            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                mTimelineView.initLine(viewType);
            }

            public void bind() {
                //mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
                mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimaryDark));

            }
        }
    }
}
