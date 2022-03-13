package ca.dal.csci3130.quickcash.EmployerJobListing;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.MyViewAction;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.home.EmployerHomeActivity;
import ca.dal.csci3130.quickcash.testConstants;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

public class EmployerJobListTest {

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


    /***
     * AT-13.1 Check button visibility
     */
    @Test
    public void MyPostButtonVisible(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        onView(withId(R.id.postingsBtn)).check(matches(withText("View Postings")));

    }
    /***
     * AT13.2 view applicants that apply for the job
     * check the first line of the first job is match
     */

    @Test
    public void ViewApplicantTest(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        //scroll to the position of the item can click the view applicant button
        onView(withId(R.id.jobsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.applicantBtn)));
        onView(withText("Employee NumberTwo")).check(matches(isDisplayed()));

    }


    /***
     * AT13.3 Select one applicants for the job
     *
     */

    @Test
    public void selectApplicantsTest(){

        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        //scroll to the position of the item can click the view applicant button
        onView(withId(R.id.jobsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.applicantBtn)));

        testConstants.waitFirebase();

        onView(withId(R.id.acceptBtn)).check(matches(withText("Accept")));

    }



    /***
     * AT13.4
     * press back button to back to homepage
     */
    @Test
    public void EmployerCancel(){
        testConstants.employerLogin();

        testConstants.waitFirebase();

        testConstants.employerViewJob();

        Espresso.pressBack();

        intended(hasComponent(EmployerHomeActivity.class.getName()));
    }
}
