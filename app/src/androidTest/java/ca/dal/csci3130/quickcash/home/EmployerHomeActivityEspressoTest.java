package ca.dal.csci3130.quickcash.home;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.getIntents;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;

import ca.dal.csci3130.quickcash.R;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;
import ca.dal.csci3130.quickcash.usermanagement.SessionManager;
import ca.dal.csci3130.quickcash.usermanagement.SessionManagerInterface;
import ca.dal.csci3130.quickcash.usermanagement.User;
import ca.dal.csci3130.quickcash.usermanagement.UserInterface;


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
    public void LogInAndPressBack(){
        onView(withId(R.id.name)).perform(typeText("Employer@hotmail.com"));
        onView(withId(R.id.password)).perform(typeText("Ab12345##"));
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(EmployerHomeActivity.class.getName()));

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.logoutBtn));
    }

    @Test
    public void LogInAndLogOut(){
        onView(withId(R.id.name)).perform(typeText("Employer@hotmail.com"));
        onView(withId(R.id.password)).perform(typeText("Ab12345##"));
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(EmployerHomeActivity.class.getName()));
        onView(withId(R.id.logoutBtn)).perform(click());

        onView(withId(R.id.name));

    }

}
