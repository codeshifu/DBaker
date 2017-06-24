package com.soundwebcraft.dbaker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class IntentTest {
    @Rule
    public IntentsTestRule<RecipeListActivity> mIntentsTestRule =
            new IntentsTestRule<>(RecipeListActivity.class);
    private String mPackageName = "com.soundwebcraft.dbaker";;

    @Test
    public void validateIntentSentToPackage () {
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(toPackage(mPackageName));
    }
}
