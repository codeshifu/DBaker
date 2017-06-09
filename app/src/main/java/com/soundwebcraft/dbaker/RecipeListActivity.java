package com.soundwebcraft.dbaker;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.soundwebcraft.dbaker.fragments.RecipeListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soundwebcraft.dbaker.R.id.toolbar;

public class RecipeListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView mToolbarTitle;
    @BindView(toolbar) Toolbar mToolbar;

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

        // host RecipeListFragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new RecipeListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
