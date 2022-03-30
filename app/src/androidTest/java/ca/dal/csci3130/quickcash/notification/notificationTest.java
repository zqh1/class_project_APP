package ca.dal.csci3130.quickcash.notification;
import android.content.Context;
import android.content.Intent;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import ca.dal.csci3130.quickcash.testConstants;
import ca.dal.csci3130.quickcash.usermanagement.LoginActivity;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class notificationTest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.example.android.testing.uiautomator.BasicSample";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice device;
    private UiDevice device2;

    @Rule
    public ActivityScenarioRule<LoginActivity> myRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.release();
        Intents.init();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device2 = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        device.pressHome();
        device2.pressHome();

        // Wait for launcher
        final String launcherPackage = device.getLauncherPackageName();
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);
        device2.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = ApplicationProvider.getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
        device2.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);

    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void checkNotificationRedirect() {


        UiObject loginBtn = device.findObject(new UiSelector()
                .text("Login")
                .className("android.widget.Button"));
        testConstants.employerLogin();
        testConstants.employeeLogin();

    }

}
