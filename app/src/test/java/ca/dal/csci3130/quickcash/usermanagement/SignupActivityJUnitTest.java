package ca.dal.csci3130.quickcash.usermanagement;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.runner.AndroidJUnit4;

public class SignupActivityJUnitTest {

    private static Signup signupActivity;

    @BeforeClass
    public static void setup() {
        signupActivity = new Signup();
    }

    @Test
    public void checkIfNameIsCorrect() {

        assertTrue(signupActivity.verifyName("Luis"));
        assertTrue(signupActivity.verifyName("test"));
        assertTrue(signupActivity.verifyName("abcdddd"));
    }

    @Test
    public void checkIfNameIsIncorrect() {

        assertFalse(signupActivity.verifyName("Luis_Cabarique"));
        assertFalse(signupActivity.verifyName("Luis**Cabarique"));
        assertFalse(signupActivity.verifyName("Luis_Cabarique254"));
        assertFalse(signupActivity.verifyName("254"));
        assertFalse(signupActivity.verifyName(" "));
    }

    @Test
    public void checkIfEmailIsCorrect() {

        assertTrue(signupActivity.verifyEmail("Luis.Cabarique@dal.ca"));
        assertTrue(signupActivity.verifyEmail("Luis@gmail.com"));
        assertTrue(signupActivity.verifyEmail("Luis_C@hotmail.com"));
    }

    @Test
    public void checkIfEmailIsIncorrect() {

        assertFalse(signupActivity.verifyEmail("Luis Cabarique@dal.ca"));
        assertFalse(signupActivity.verifyEmail("Luis@gmail"));
        assertFalse(signupActivity.verifyEmail("Luis_C*hotmail.com"));
        assertFalse(signupActivity.verifyEmail("Luis_C*hotmail.456"));
    }

    @Test
    public void checkIfPasswordIsStrong() {

        assertTrue(signupActivity.verifyPassword("Ab345&78"));
        assertTrue(signupActivity.verifyPassword("abC123#__ww"));
        assertTrue(signupActivity.verifyPassword("7896123bCC#"));
    }

    @Test
    public void checkIfPasswordIsWeak() {

        assertFalse(signupActivity.verifyPassword("abc"));
        assertFalse(signupActivity.verifyPassword("789456123aabbCC"));
        assertFalse(signupActivity.verifyPassword("123456789"));
        assertFalse(signupActivity.verifyPassword("AAAAAAAAAAA"));
        assertFalse(signupActivity.verifyPassword("aB1#"));
    }

    @Test
    public void checkIfPhoneIsCorrect() {

        assertTrue(signupActivity.verifyPhone("1234567890"));
        assertTrue(signupActivity.verifyPhone("7822345605"));
        assertTrue(signupActivity.verifyPhone("5065317485"));
    }

    @Test
    public void checkIfPhoneIsIncorrect() {

        assertFalse(signupActivity.verifyPhone("123456789"));
        assertFalse(signupActivity.verifyPhone("abcabcabca"));
        assertFalse(signupActivity.verifyPhone("192-1927-123"));
    }
}
