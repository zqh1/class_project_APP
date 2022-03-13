package ca.dal.csci3130.quickcash.home;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.jobmanagement.JobActivity;
import ca.dal.csci3130.quickcash.testConstants;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

public class EmployerHomeActivityEspressoTest {

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
    public void LogInAndPressBack() {

        testConstants.employerLogin();

        testConstants.waitFirebase();

        intended(hasComponent(EmployerHomeActivity.class.getName()));

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.logoutBtn)).check(matches(withText("LOGOUT")));
    }

    @Test
    public void LogInAndLogOut() {

        testConstants.employerLogin();

        testConstants.waitFirebase();

        intended(hasComponent(EmployerHomeActivity.class.getName()));

        onView(withId(R.id.logoutBtn)).perform(click());

        onView(withId(R.id.name)).check(matches(withText("")));
    }

    @Test
    public void checkCreateJobButton(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        onView(withId(R.id.createJobBtn)).check(matches(withText("CREATE NEW JOB")));
    }

    @Test
    public void checkCreateJobButtonRedirect(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        onView(withId(R.id.createJobBtn)).perform(click());
        intended(hasComponent(JobActivity.class.getName()));

        onView(withId(R.id.jobCreationLabel)).check(matches(withText("Job's Details:")));
    }

}
