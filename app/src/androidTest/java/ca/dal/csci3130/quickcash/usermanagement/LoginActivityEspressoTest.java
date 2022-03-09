package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployeeHomeActivity;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.testConstants;

public class LoginActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> testRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.release();
        Intents.init();
    }

    @AfterClass

    public static void tearDown() {
        System.gc();
    }

    @Test
    public void checkCorrectInfoEmployee() {

        testConstants.employeeLogin();

        testConstants.waitFirebase();

        onView(withId(R.id.employeeLabel)).check(matches(withText("WELCOME EMPLOYEE")));
    }

    @Test
    public void checkIncorrectInfoEmployee() {

        onView(withId(R.id.name)).perform(typeText("Employee@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("incorrectPass"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        testConstants.waitFirebase();

        onView(withId(R.id.loginBtn)).check(matches(withText("LOGIN")));
    }

    @Test
    public void checkCorrectInfoEmployeeRedirect() {

        testConstants.employeeLogin();

        testConstants.waitFirebase();

        intended(hasComponent(EmployeeHomeActivity.class.getName()));

        onView(withId(R.id.logoutBtn)).check(matches(withText("LOGOUT")));
    }

    @Test
    public void checkCorrectInfoEmployer() {

        testConstants.employerLogin();

        testConstants.waitFirebase();

        onView(withId(R.id.employerLabel)).check(matches(withText("WELCOME EMPLOYER")));
    }

    @Test
    public void checkIncorrectInfoEmployer() {

        onView(withId(R.id.name)).perform(typeText("employer@dal.ca"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("incorrectPass"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        testConstants.waitFirebase();

        onView(withId(R.id.loginBtn)).check(matches(withText("LOGIN")));
    }

    @Test
    public void checkCorrectInfoEmployerRedirect() {

        testConstants.employerLogin();

        testConstants.waitFirebase();

        intended(hasComponent(EmployerHomeActivity.class.getName()));

        onView(withId(R.id.logoutBtn)).check(matches(withText("LOGOUT")));
    }

    @Test
    public void checkLogoutEmployeeRedirect() {

        testConstants.employeeLogin();

        onView(withId(R.id.logoutBtn)).perform(click());

        testConstants.waitFirebase();

        intended(hasComponent(LoginActivity.class.getName()));

        onView(withId(R.id.name)).check(matches(withText("")));
    }

    @Test
    public void checkLogoutEmployerRedirect() {

        testConstants.employerLogin();

        testConstants.waitFirebase();

        onView(withId(R.id.logoutBtn)).perform(click());

        intended(hasComponent(LoginActivity.class.getName()));

        onView(withId(R.id.name)).check(matches(withText("")));
    }
}
