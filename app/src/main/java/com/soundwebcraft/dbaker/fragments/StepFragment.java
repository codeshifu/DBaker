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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class StepFragment extends Fragment {
    @BindView(R.id.recyclerview)
    EmptyStateRecyclerView mRecyclerview;
    @BindView(R.id.empty_state_feedback)
    TextView mEmptyStateFeedback;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    private Unbinder unbinder;
    private Context mContext;

    public StepFragment() {
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
        View v = inflater.inflate(R.layout.fragment_step_list, container, false);
        unbinder = ButterKnife.bind(this, v);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setNestedScrollingEnabled(false);
        mRecyclerview.setAdapter(new StepAdapter(mContext));

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
        private Context adapterContext;

        public StepAdapter(Context context) {
            adapterContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(adapterContext);
            View v = inflater.inflate(R.layout.list_item_step, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return 8;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bind() {

            }
        }
    }
}
