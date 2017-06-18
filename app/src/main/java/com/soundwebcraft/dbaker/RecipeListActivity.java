package com.soundwebcraft.dbaker;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.fragments.DetailFragment;
import com.soundwebcraft.dbaker.fragments.RecipeListFragment;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soundwebcraft.dbaker.R.id.toolbar;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.OnListItemSelectedListener {

    @BindView(R.id.toolbar_title) TextView mToolbarTitle;
    @BindView(toolbar) Toolbar mToolbar;

    private boolean isMultiPane = false;
    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

        Typeface courgette = Typeface.createFromAsset(getAssets(), getString(R.string.custom_font_name));
        mToolbarTitle.setTypeface(courgette);

        determinePaneLayout();

        // host RecipeListFragment
        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new RecipeListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void determinePaneLayout() {
        if (findViewById(R.id.detail_fragment) != null) {
            isMultiPane = true;
        }
    }

    @Override
    public void onItemSelected(Recipe recipe) {
        if (isMultiPane) {
            DetailFragment detailFragment = DetailFragment.newInstance(recipe);
            fm.beginTransaction()
                    .replace(R.id.detail_fragment, detailFragment)
                    .commit();
        } else {
            // For phone, launch detail activity using intent
            Intent i = new Intent(this, RecipeDetailActivity.class);
            // Embed the serialized item
            i.putExtra(RecipeDetailActivity.EXTRA_RECIPE, Parcels.wrap(recipe));
            // Start the activity
            startActivity(i);
        }
    }
}
