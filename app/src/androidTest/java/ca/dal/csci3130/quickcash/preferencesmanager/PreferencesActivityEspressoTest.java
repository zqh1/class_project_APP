package ca.dal.csci3130.quickcash.preferencesmanager;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
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
import ca.dal.csci3130.quickcash.testConstants;

public class PreferencesActivityEspressoTest {
    @Rule
    public ActivityScenarioRule<PreferencesActivity> testRule = new ActivityScenarioRule<>(PreferencesActivity.class);

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
    public void checkIfLabelsAreVisible() {
        onView(withId(R.id.preferencesFilling)).check(matches(withText("")));
        onView(withId(R.id.salaryMinimal)).check(matches(withText("")));
        onView(withId(R.id.startingHour)).check(matches(withText("")));
        onView(withId(R.id.durationFill)).check(matches(withText("")));
        onView(withId(R.id.maxDistanceFill)).check(matches(withText("")));
    }

    @Test
    public void checkCorrectJob() {
        onView(withId(R.id.preferencesFilling)).perform(typeText("ABCDE"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());

        testConstants.waitFirebase();

        onView(withId(R.id.employeeLabel)).check(matches(withText("WELCOME EMPLOYEE")));
    }

    @Test
    public void checkCorrectSalary(){
        onView(withId(R.id.salaryMinimal)).perform(typeText("13.35"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.salaryMinimal)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectSalary(){
        onView(withId(R.id.salaryMinimal)).perform(typeText("13"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.salaryMinimal)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectStartingTime(){
        onView(withId(R.id.startingHour)).perform(typeText("13:35"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.startingHour)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectStartingTime(){
        onView(withId(R.id.startingHour)).perform(typeText("24:00"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.startingHour)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectDuration(){
        onView(withId(R.id.durationFill)).perform(typeText("24"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.durationFill)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectDuration(){
        onView(withId(R.id.durationFill)).perform(typeText("892"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.durationFill)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectDistance(){
        onView(withId(R.id.maxDistanceFill)).perform(typeText("10"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.maxDistanceFill)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectDistance(){
        onView(withId(R.id.maxDistanceFill)).perform(typeText("0"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());
        onView(withId(R.id.maxDistanceFill)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkIfCorrectInfoEntered(){
        onView(withId(R.id.preferencesFilling)).perform(typeText("TryMe"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.salaryMinimal)).perform(typeText("99.99"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.startingHour)).perform(typeText("00:00"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.durationFill)).perform(typeText("12"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.maxDistanceFill)).perform(typeText("19"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.proceedPreferBtn)).perform(click());

        testConstants.waitFirebase();

        onView(withId(R.id.employeeLabel)).check(matches(withText("WELCOME EMPLOYEE")));
    }

    @Test
    public void cancelButtonClicked(){
        onView(withId(R.id.cancelPreferBtn)).perform(click());

        testConstants.waitFirebase();

        onView(withId(R.id.employeeLabel)).check(matches(withText("WELCOME EMPLOYEE")));
    }
}
