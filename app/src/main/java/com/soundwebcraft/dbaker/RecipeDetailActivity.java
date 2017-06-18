package com.soundwebcraft.dbaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.soundwebcraft.dbaker.data.model.Recipe;
import com.soundwebcraft.dbaker.fragments.DetailFragment;

import org.parceler.Parcels;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        // hide default actionbar title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

        Intent otherIntent = getIntent();
        if (otherIntent == null) throw new NullPointerException(getString(R.string.null_intent));

        Recipe recipe = Parcels.unwrap(otherIntent.getParcelableExtra(EXTRA_RECIPE));
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.detail_fragment);
        if (fragment == null) {
            fragment = DetailFragment.newInstance(recipe);
            fm.beginTransaction()
                    .add(R.id.detail_fragment, fragment)
                    .commit();
        }
    }
}
