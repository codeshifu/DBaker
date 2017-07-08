package com.soundwebcraft.dbaker.fragments;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soundwebcraft.dbaker.R;
import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.widget.CollectionWidget;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailFragment extends Fragment {

    private static final String RECIPE_EXTRA = "recipe_extra";
    @BindView(R.id.app_bar_image)
    ImageView heroImage;
    //    @BindView(R.id.app_bar_toolbar_title)
//    TextView mToolbarTitle;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    private Unbinder unbinder;
    private Recipe recipe;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        recipe = Parcels.unwrap(getArguments().getParcelable(RECIPE_EXTRA));
        saveLastViewedRecipe(context);
    }

    private void saveLastViewedRecipe(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(getString(R.string.pref_name), 0).edit();
        editor.putString(getString(R.string.pref_key), String.valueOf(recipe.getId()));
        editor.apply();

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        if (widgetManager != null) {
            ComponentName componentName = new ComponentName(context, CollectionWidget.class);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(componentName);
            widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        AppCompatActivity activity = ((AppCompatActivity) getContext());
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbar.setTitle(recipe.getName());
        Typeface courgette = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.custom_font_name));
        mCollapsingToolbar.setCollapsedTitleTypeface(courgette);
        mCollapsingToolbar.setExpandedTitleTypeface(courgette);

        heroImage.setImageResource(recipe.getImageResourceId());

        mViewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return IngredientListFragment.newInstance(recipe.getIngredients());
                    case 1:
                        return StepFragment.newInstance(recipe.getSteps());
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.tab_ingredient);
                    case 1:
                        return getString(R.string.tab_step);
                    default:
                        return null;
                }
            }
        });
        // attach tabLayout to viewpager
        mTab.setupWithViewPager(mViewpager);

        return view;
    }

    public static DetailFragment newInstance(Recipe recipe) {

        Bundle args = new Bundle();
        args.putParcelable(RECIPE_EXTRA, Parcels.wrap(recipe));
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
