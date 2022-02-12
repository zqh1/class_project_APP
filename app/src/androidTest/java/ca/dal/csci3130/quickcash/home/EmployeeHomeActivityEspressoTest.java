package ca.dal.csci3130.quickcash.home;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

public class EmployeeHomeActivityEspressoTest {
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
        onView(withId(R.id.name)).perform(typeText("Employee@hotmail.com"));
        onView(withId(R.id.password)).perform(typeText("Ab12345##"));
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(EmployeeHomeActivity.class.getName()));

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.logoutBtn));
    }

    @Test
    public void LogInAndLogOut(){
        onView(withId(R.id.name)).perform(typeText("Employee@hotmail.com"));
        onView(withId(R.id.password)).perform(typeText("Ab12345##"));
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(EmployeeHomeActivity.class.getName()));
        onView(withId(R.id.logoutBtn)).perform(click());

        onView(withId(R.id.name));

    }
}
