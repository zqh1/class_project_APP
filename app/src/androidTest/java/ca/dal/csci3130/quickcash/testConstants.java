package ca.dal.csci3130.quickcash;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;

public class testConstants {

    //Disable class instantiation
    private testConstants() {}

    /**
     * Reusable method to login with the employee
     */
    public static void employeeLogin() {
        onView(withId(R.id.name)).perform(typeText("employee@dal.ca"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
    }

    /**
     * Reusable method to login with the employer
     */
    public static void employerLogin() {
        onView(withId(R.id.name)).perform(typeText("employer@dal.ca"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginBtn)).perform(click());
    }

    /**
     * Reusable method to set timer to wait to firebase to respond
     */
    public static void waitFirebase() {

        //PLEASE IF ANYONE KNOWS HOW TO MAKE SOMETHING BETTER, PLEASE CHANGE IT
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
