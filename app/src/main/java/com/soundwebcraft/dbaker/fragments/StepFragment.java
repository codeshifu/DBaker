package com.soundwebcraft.dbaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.soundwebcraft.dbaker.PlayerActivity;
import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;
import com.soundwebcraft.dbaker.utils.VectorDrawableUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.soundwebcraft.dbaker.data.model.Recipe.Steps;


public class StepFragment extends Fragment {
    public static final String STEP_EXTRA = "steps";
    @BindView(R.id.recyclerview)
    EmptyStateRecyclerView mRecyclerview;
    @BindView(R.id.empty_state_feedback)
    TextView mEmptyStateFeedback;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    private Unbinder unbinder;
    private Context mContext;
    private List<Steps> mStepList;

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

        mStepList = Parcels.unwrap(getArguments().getParcelable(STEP_EXTRA));

        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setNestedScrollingEnabled(false);
        mRecyclerview.setAdapter(new StepAdapter(mContext, mStepList));

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static StepFragment newInstance(List<Steps> steps) {
        Bundle args = new Bundle();
        args.putParcelable(STEP_EXTRA, Parcels.wrap(steps));
        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
        private Context adapterContext;
        private List<Steps> mStepList;

        public StepAdapter(Context context, List<Steps> steps) {
            adapterContext = context;
            mStepList = steps;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(adapterContext);
            View v = inflater.inflate(R.layout.list_item_step, parent, false);

            return new ViewHolder(v, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Steps step = mStepList.get(position);
            holder.bind(step);
        }

        @Override
        public int getItemViewType(int position) {
            return TimelineView.getTimeLineViewType(position, getItemCount());
        }

        @Override
        public int getItemCount() {
            return mStepList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            @BindView(R.id.time_marker)
            TimelineView mTimelineView;
            @BindView(R.id.short_description)
            TextView tvShortDescription;
            @BindView(R.id.description)
            TextView tvDescription;
            @BindView(R.id.step_thumbnail)
            ImageView stepThumbnailView;


            ViewHolder(View itemView, int viewType) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                mTimelineView.initLine(viewType);
                itemView.setOnClickListener(this);
            }

            void bind(Steps step) {
                if (step != null) {
                    tvShortDescription.setText(step.getShortdescription());
                    tvDescription.setText(step.getDescription());
                    if (TextUtils.isEmpty(step.getVideourl())) {
                        mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_radio, R.color.colorPrimaryDark));
                    } else {
                        mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_play, R.color.colorPrimaryDark));
                    }
                    if (TextUtils.isEmpty(step.getThumbnailurl())) {
                        stepThumbnailView.setVisibility(View.GONE);
                    } else {
                        Picasso.with(mContext)
                                .load(step.getThumbnailurl())
                                .error(R.drawable.no_preview)
                                .into(stepThumbnailView);
                    }
                }
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Steps step = mStepList.get(position);
                String videoURL = step.getVideourl();
                if (!TextUtils.isEmpty(videoURL)) {
                    Intent intent = new Intent(mContext, PlayerActivity.class);
                    intent.putExtra(PlayerActivity.VIDEO_EXTRA, videoURL);
                    startActivity(intent);
                } else {
                    final Snackbar snackbar = Snackbar.make(v, getString(R.string.no_video), Snackbar.LENGTH_LONG);
                    snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        }
    }
}
