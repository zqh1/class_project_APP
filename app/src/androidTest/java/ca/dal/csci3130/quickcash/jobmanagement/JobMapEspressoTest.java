package ca.dal.csci3130.quickcash.jobmanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.MyViewAction;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.testConstants;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

public class JobMapEspressoTest {

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
    public void ViewJobMapButtonTest(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        testConstants.waitFirebase();

        //Verify that the map button exists
        onView(withId(R.id.jobsRecyclerView)).check(matches(hasDescendant(withText("VIEW JOB LOCATION"))));
    }

    @Test
    public void ViewJobMapTest(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        testConstants.waitFirebase();

        //scroll to the position of the item can click the view map button
        onView(withId(R.id.jobsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.mapBtn)));

        testConstants.waitFirebase();

        //Verify that the map screen is there
        onView(withId(R.id.returnBtn)).check(matches(withText("RETURN")));
    }

    @Test
    public void jobMapReturnTest(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        testConstants.waitFirebase();

        //scroll to the position of the item can click the view map button
        onView(withId(R.id.jobsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.mapBtn)));

        testConstants.waitFirebase();

        //Click on return to exit map
        onView(withId(R.id.returnBtn)).perform(click());

        testConstants.waitFirebase();

        //Verify that the map button exists
        onView(withId(R.id.jobsRecyclerView)).check(matches(hasDescendant(withText("VIEW JOB LOCATION"))));
    }
}
