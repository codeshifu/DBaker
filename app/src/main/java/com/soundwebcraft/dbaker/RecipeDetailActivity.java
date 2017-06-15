package com.soundwebcraft.dbaker;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.fragments.IngredientListFragment;
import com.soundwebcraft.dbaker.fragments.StepFragment;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "recipe";
    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.app_bar_image)
    ImageView heroImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // hide default actionbar title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

        Typeface courgette = Typeface.createFromAsset(getAssets(), getString(R.string.custom_font_name));
        mToolbarTitle.setTypeface(courgette);

        Intent otherIntent = getIntent();
        if (otherIntent == null) throw new NullPointerException(getString(R.string.null_intent));

        final Recipe recipe = Parcels.unwrap(otherIntent.getParcelableExtra(EXTRA_RECIPE));
        mToolbarTitle.setText(recipe.getName());
        heroImage.setImageResource(recipe.getImageResourceId());

        fm = getSupportFragmentManager();

        mViewpager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return IngredientListFragment.newInstance(recipe.getIngredients());
                    case 1:
                        return new StepFragment();
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
    }
}
