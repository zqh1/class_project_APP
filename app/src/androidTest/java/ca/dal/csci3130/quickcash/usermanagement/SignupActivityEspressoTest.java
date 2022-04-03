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

import androidx.annotation.NonNull;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.testConstants;

public class SignupActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<SignupActivity> myRule = new ActivityScenarioRule<>(SignupActivity.class);

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
        onView(withId(R.id.lastNameInput)).perform(typeText("Gus"), ViewActions.closeSoftKeyboard());
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

        //Delete user if exits on database
        new UserDAOAdapter(new UserDAO()).getDatabaseReference().orderByChild("email").equalTo("test2@dal.ca").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.hasChildren())
                    new UserDAOAdapter(new UserDAO()).getDatabaseReference().child(Objects.requireNonNull(snapshot.getValue()).toString().substring(1, 21)).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        onView(withId(R.id.firstNameInput)).perform(typeText("DELETE"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.lastNameInput)).perform(typeText("THIS"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.emailInput)).perform(typeText("test2@dal.ca"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.phoneInput)).perform(typeText("0123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());

        testConstants.waitFirebase();

        //Check that screen change to login screen (only happens if account is created successfully)
        intended(hasComponent(LoginActivity.class.getName()));

        onView(withId(R.id.loginBtn)).check(matches(withText("LOGIN")));
    }

    @Test
    public void checkIfDuplicatedAccount() {

        //NOTE: Test depends on the account was already created, if deleted by mistake, just run
        //      this test twice (first run will create the account and fail as expected)

        onView(withId(R.id.firstNameInput)).perform(typeText("Test"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.lastNameInput)).perform(typeText("User"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.emailInput)).perform(typeText("test1@dal.ca"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordInput)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordInput)).perform(typeText("Ab12345##"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.phoneInput)).perform(typeText("0123456789"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signUpBtn)).perform(click());

        testConstants.waitFirebase();

        onView(withId(R.id.emailInput)).check(matches(hasTextColor(R.color.red)));
    }
}
