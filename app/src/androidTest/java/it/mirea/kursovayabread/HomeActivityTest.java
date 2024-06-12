package it.mirea.kursovayabread;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule =
            new ActivityScenarioRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        // Set up any required resources before each test
    }

    @After
    public void tearDown() {
        // Clean up resources after each test
    }

    @Test
    public void testHomeActivityElements() {
        onView(withId(R.id.drawer_layout)).check(doesNotExist());
        onView(withId(R.id.navigation_view)).check(doesNotExist());
        onView(withId(R.id.navigation_bar)).check(doesNotExist());
    }
}