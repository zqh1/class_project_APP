package ca.dal.csci3130.quickcash.feedback;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ca.dal.csci3130.quickcash.MyViewAction;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.testConstants;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

public class GiveFeedbackActivityEspressoTest {

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
    public void employerFeedbackButtonExistAndSubmit() {
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        testConstants.waitFirebase();

        onView(withId(R.id.jobsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.feedbackBtn)));

        onView(withId(R.id.ratingTitle)).check(matches(withText("Please rate this person")));

        onView(withId(R.id.submitButton)).perform(click());

        testConstants.waitFirebase();
    }

    @Test
    public void employeeFeedbackButtonExistAndSubmit() {
        testConstants.employeeLogin();

        testConstants.waitFirebase();

        testConstants.employeeViewJob();

        testConstants.waitFirebase();

        onView(withId(R.id.jobsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.feedbackBtn)));

        onView(withId(R.id.ratingTitle)).check(matches(withText("Please rate this person")));

        onView(withId(R.id.submitButton)).perform(click());

        testConstants.waitFirebase();
    }
}
