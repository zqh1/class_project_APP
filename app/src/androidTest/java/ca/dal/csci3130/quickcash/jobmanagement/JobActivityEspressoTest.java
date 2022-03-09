package ca.dal.csci3130.quickcash.jobmanagement;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ca.dal.csci3130.quickcash.R;

public class JobActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<JobActivity> myRule = new ActivityScenarioRule<>(JobActivity.class);

    @BeforeClass
    public static void setup() {
        Intents.init();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void checkIfScreenFieldsAvailable() {

        onView(withId(R.id.titleInput)).check(matches(withText("")));
        onView(withId(R.id.descriptionInput)).check(matches(withText("")));
        onView(withId(R.id.dateBtn)).check(matches(withText("DATE")));
        onView(withId(R.id.timeBtn)).check(matches(withText("TIME")));
        onView(withId(R.id.durationInput)).check(matches(withText("")));
        onView(withId(R.id.salaryInput)).check(matches(withText("")));
        onView(withId(R.id.tagsInput)).check(matches(withText("")));
    }
}
