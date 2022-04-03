package ca.dal.csci3130.quickcash.payment;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ca.dal.csci3130.quickcash.MyViewAction;

import androidx.recyclerview.widget.RecyclerView;
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

public class PayActivityEspressoTest {

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
    public void checkPayButtonTest() {
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        testConstants.waitFirebase();

        onView(withId(R.id.jobsRecyclerView)).check(matches(hasDescendant(withText("PAY"))));
    }

    @Test
    public void checkPayButtonRedirect() {
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        testConstants.waitFirebase();

        onView(withId(R.id.jobsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.paymentBtn)));

        testConstants.waitFirebase();

        onView(withId(R.id.idTVStatus)).check(matches(withText("")));

    }
}

