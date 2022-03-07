package ca.dal.csci3130.quickcash.jobmanagement;

import static org.junit.Assert.fail;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
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

    }
}
