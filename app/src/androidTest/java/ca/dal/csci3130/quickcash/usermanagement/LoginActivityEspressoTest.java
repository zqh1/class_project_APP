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
    public void checkCorrectInfoEmployee(){

        onView(withId(R.id.name)).perform(typeText("Employee@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.employeeLabel)).check(matches(withText("WELCOME EMPLOYEE")));
    }

    @Test
    public void checkIncorrectInfoEmployee(){

        onView(withId(R.id.name)).perform(typeText("Employee@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("incorrectPass"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.loginBtn));

    }

    @Test
    public void checkCorrectInfoEmployeeRedirect(){

        onView(withId(R.id.name)).perform(typeText("Employee@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(EmployeeHomeActivity.class.getName()));
    }

    @Test
    public void checkCorrectInfoEmployer(){



        onView(withId(R.id.name)).perform(typeText("Employer@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.employerLabel)).check(matches(withText("WELCOME EMPLOYER")));

    }

    @Test
    public void checkIncorrectInfoEmployer(){

        onView(withId(R.id.name)).perform(typeText("Employer@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("incorrectPass"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.loginBtn));


    }

    @Test
    public void checkCorrectInfoEmployerRedirect(){

        onView(withId(R.id.name)).perform(typeText("Employer@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(EmployerHomeActivity.class.getName()));

    }

    @Test
    public void checkLogoutEmployeeRedirect(){

        onView(withId(R.id.name)).perform(typeText("Employee@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());


        onView(withId(R.id.logoutBtn)).perform(click());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(LoginActivity.class.getName()));



    }

    @Test
    public void checkLogoutEmployerRedirect(){
        onView(withId(R.id.name)).perform(typeText("Employer@hotmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.logoutBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(LoginActivity.class.getName()));
    }

}
