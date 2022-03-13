package ca.dal.csci3130.quickcash.preferencesmanager;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;



import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;

import ca.dal.csci3130.quickcash.testConstants;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;




public class PreferencesActivitySearchEspressoTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> myRule = new ActivityScenarioRule<>(LoginActivity.class);

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
    public void checkSuccessfulSearch(){
        testConstants.employeeLogin();
        testConstants.waitFirebase();
        onView(withId(R.id.searchJobButton)).perform(click());
        onView(withId(R.id.preferencesFilling)).perform(typeText("job1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());

        onView(withId(R.id.jobsRecyclerView)).check(matches(hasDescendant(withText("job1"))));
        testConstants.waitFirebase();
    }

    @Test
    public void checkUnsuccessfulSearch(){
        testConstants.employeeLogin();
        testConstants.waitFirebase();
        onView(withId(R.id.searchJobButton)).perform(click());
        onView(withId(R.id.preferencesFilling)).perform(typeText("DNE"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());

        intended(hasComponent(PreferencesActivity.class.getName()));

        onView(withId(R.id.preferencesFilling)).check(matches(withText("Search Parameters")));
    }

    @Test
    public void checkNoParameters(){
        testConstants.employeeLogin();
        testConstants.waitFirebase();
        onView(withId(R.id.searchJobButton)).perform(click());
        onView(withId(R.id.proceedPreferBtn)).perform(click());

        onView(withId(R.id.jobsRecyclerView)).check(matches(hasDescendant(withText("job1"))));
        onView(withId(R.id.jobsRecyclerView)).check(matches(hasDescendant(withText("job2"))));
        onView(withId(R.id.jobsRecyclerView)).check(matches(hasDescendant(withText("job 3"))));
    }
}
