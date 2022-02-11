package ca.dal.csci3130.quickcash.usermanagement;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        assertTrue(Signup.verifyEmail("Luis.Cabarique@dal.ca"));
        assertTrue(Signup.verifyEmail("Luis@gmail.com"));
        assertTrue(Signup.verifyEmail("Luis_C@hotmail.com"));
    }

    @Test
    public void checkIfEmailIsIncorrect() {

        assertFalse(Signup.verifyEmail("Luis Cabarique@dal.ca"));
        assertFalse(Signup.verifyEmail("Luis@gmail"));
        assertFalse(Signup.verifyEmail("Luis_C*hotmail.com"));
        assertFalse(Signup.verifyEmail("Luis_C*hotmail.456"));
    }

    @Test
    public void checkIfPasswordIsStrong() {

        assertTrue(Signup.verifyPassword("Ab345&78"));
        assertTrue(Signup.verifyPassword("abC123#__ww"));
        assertTrue(Signup.verifyPassword("7896123bCC#"));
    }

    @Test
    public void checkIfPasswordIsWeak() {

        assertFalse(Signup.verifyPassword("abc"));
        assertFalse(Signup.verifyPassword("789456123aabbCC"));
        assertFalse(Signup.verifyPassword("123456789"));
        assertFalse(Signup.verifyPassword("AAAAAAAAAAA"));
        assertFalse(Signup.verifyPassword("aB1#"));
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
