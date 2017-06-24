package com.soundwebcraft.dbaker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.soundwebcraft.dbaker.utils.EmptyStateRecyclerView;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;

public class RecyclerViewTest {
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void checkLayoutHasRecyclerView() {
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerViewIsInstanceOfEmptyRecyclerView() {
        onView(withId(R.id.recyclerview)).check(matches(instanceOf(EmptyStateRecyclerView.class)));
    }

    @Test
    public void scrollToFirstItem() {
        // First, scroll to the view holder using the isInTheMiddle matcher.
        onView(ViewMatchers.withId(R.id.recyclerview))
                .perform(RecyclerViewActions.scrollToPosition(0));
    }
}
