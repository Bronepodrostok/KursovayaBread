package it.mirea.kursovayabread;

import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Set up any required resources before each test
    }

    @After
    public void tearDown() {
        // Clean up resources after each test
    }

    @Test
    public void testSignInButton() {
        onView(withId(R.id.btnSignIn)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSignIn)).perform(click());
        // Add more assertions to verify the sign-in window is displayed
    }

    @Test
    public void testRegisterButton() {
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()));
        onView(withId(R.id.btnRegister)).perform(click());
        // Add more assertions to verify the registration window is displayed
    }

    @Test
    public void testSignInEmailHint() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.email_field)).check(matches(withHint("Email")));
    }

    @Test
    public void testSignInPasswordHint() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.pass_field)).check(matches(withHint("Пароль")));
    }

    @Test
    public void testRegisterEmailHint() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.email_field)).check(matches(withHint("Email")));
    }

    @Test
    public void testRegisterPasswordHint() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.pass_field)).check(matches(withHint("Пароль")));
    }

    @Test
    public void testRegisterNameHint() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.name_field)).check(matches(withHint("Имя")));
    }

    @Test
    public void testRegisterPhoneHint() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.phone_field)).check(matches(withHint("Телефон")));
    }

    @Test
    public void testSignInEmailInput() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.email_field)).perform(replaceText("test@example.com"));
        onView(withId(R.id.email_field)).check(matches(withText("test@example.com")));
    }

    @Test
    public void testSignInPasswordInput() {
        onView(withId(R.id.btnSignIn)).perform(click());
        onView(withId(R.id.pass_field)).perform(replaceText("password123"));
        onView(withId(R.id.pass_field)).check(matches(withText("password123")));
    }

    @Test
    public void testRegisterEmailInput() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.email_field)).perform(replaceText("test@example.com"));
        onView(withId(R.id.email_field)).check(matches(withText("test@example.com")));
    }

    @Test
    public void testRegisterPasswordInput() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.pass_field)).perform(replaceText("password123"));
        onView(withId(R.id.pass_field)).check(matches(withText("password123")));
    }

    @Test
    public void testRegisterNameInput() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.name_field)).perform(replaceText("John Doe"));
        onView(withId(R.id.name_field)).check(matches(withText("John Doe")));
    }

    @Test
    public void testRegisterPhoneInput() {
        onView(withId(R.id.btnRegister)).perform(click());
        onView(withId(R.id.phone_field)).perform(replaceText("1234567890"));
        onView(withId(R.id.phone_field)).check(matches(withText("1234567890")));
    }
}