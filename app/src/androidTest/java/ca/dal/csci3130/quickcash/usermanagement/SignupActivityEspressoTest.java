package ca.dal.csci3130.quickcash.usermanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.dal.csci3130.quickcash.MainActivity;
import ca.dal.csci3130.quickcash.R;

@RunWith(AndroidJUnit4.class)
public class SignupActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> myRule = new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void checkIfSignUpLabelsAreVisible() {
        onView(withId(R.id.firstNameInput)).check(matches(withText("")));
        onView(withId(R.id.lastNameInput)).check(matches(withText("")));
        onView(withId(R.id.emailInput)).check(matches(withText("")));
        onView(withId(R.id.passwordInput)).check(matches(withText("")));
        onView(withId(R.id.confirmPasswordInput)).check(matches(withText("")));
        onView(withId(R.id.userTypeInput)).check(matches(withSpinnerText("Employee")));
    }

    @Test
    public void checkIncorrectFirstName() {
        onView(withId(R.id.firstNameInput)).perform(typeText("1234"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.firstNameInput)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectFirstName() {
        onView(withId(R.id.firstNameInput)).perform(typeText("Luis"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.firstNameInput)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectLastName() {
        onView(withId(R.id.lastNameInput)).perform(typeText("12luis__"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.lastNameInput)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectLastName() {
        onView(withId(R.id.lastNameInput)).perform(typeText("Cabarique"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.lastNameInput)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectEmail() {
        onView(withId(R.id.emailInput)).perform(typeText("test@dal.dal.dal.dal"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.emailInput)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectEmail() {
        onView(withId(R.id.emailInput)).perform(typeText("test@dal.ca"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.emailInput)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectPassword() {
        onView(withId(R.id.passwordInput)).perform(typeText("123ab__"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.passwordInput)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectPassword() {
        onView(withId(R.id.passwordInput)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.passwordInput)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectConfirmingPassword() {
        onView(withId(R.id.passwordInput)).perform(typeText("123ab__"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("123ab__AA"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.confirmPasswordInput)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectConfirmingPassword() {
        onView(withId(R.id.passwordInput)).perform(typeText("Ab12bb345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("Ab12bb345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.confirmPasswordInput)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIncorrectPhone() {
        onView(withId(R.id.phoneInput)).perform(typeText("abc4561230"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.phoneInput)).check(matches(hasTextColor(R.color.red)));
    }

    @Test
    public void checkCorrectPhone() {
        onView(withId(R.id.phoneInput)).perform(typeText("7894561230"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());
        onView(withId(R.id.phoneInput)).check(matches(hasTextColor(R.color.grey)));
    }

    @Test
    public void checkIfValidInformationUserCreate() {

        //NOTE: DELETE TEST USER "test2@dal.ca" FROM FIREBASE FOR THIS TEST TO WORK,
        //      OR DUPLICATED ACCOUNT WILL FAIL THE TEST!

        onView(withId(R.id.firstNameInput)).perform(typeText("Test"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.lastNameInput)).perform(typeText("User"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.emailInput)).perform(typeText("test2@dal.ca"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.phoneInput)).perform(typeText("0123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());

        //Add 5 second wait to allow firebase to respond with email query
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check that screen change to login screen (only happens if account is created successfully)
        intended(hasComponent(LoginActivity.class.getName()));
    }

}
