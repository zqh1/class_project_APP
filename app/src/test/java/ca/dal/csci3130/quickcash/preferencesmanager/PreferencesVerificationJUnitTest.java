package ca.dal.csci3130.quickcash.preferencesmanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class PreferencesVerificationJUnitTest {
    private static PreferencesVerification verification;

    @BeforeClass
    public static void startJobVerification() {
        verification = new PreferencesVerification();
    }

    @Test
    public void checkIfTitleCorrect() {
        assertTrue(verification.validJob("Clean a pc"));
        assertTrue(verification.validJob("Cook supper"));
        assertTrue(verification.validJob("Move heavy furniture"));
    }

    @Test
    public void checkIfTitleIncorrect() { assertFalse(verification.validJob("")); }

    @Test
    public void checkIfSalaryCorrect(){
        assertTrue(verification.validSalary(20));
        assertTrue(verification.validSalary(19.12));
        assertTrue(verification.validSalary(10000.00));
        assertTrue(verification.validSalary(13.35));
    }

    @Test
    public void checkIfSalaryIncorrect(){
        assertFalse(verification.validSalary(0));
        assertFalse(verification.validSalary(100000.1));
        assertFalse(verification.validSalary(13.34));
    }

    @Test
    public void checkIfStartingTimeCorrect(){
        assertTrue(verification.validStartingTime("0:0"));
        assertTrue(verification.validStartingTime("11:59"));
        assertTrue(verification.validStartingTime("12:1"));
        assertTrue(verification.validStartingTime("23:59"));
    }

    @Test
    public void checkIfStartingTimeIncorrect(){
        assertFalse(verification.validStartingTime("24:0"));
        assertFalse(verification.validStartingTime("2.0"));
        assertFalse(verification.validStartingTime("-12:0"));
        assertFalse(verification.validStartingTime("-1200"));
        assertFalse(verification.validStartingTime("1800"));
    }

    @Test
    public void checkIfMaxDistanceCorrect(){
        assertTrue(verification.validMaxDistance(12));
        assertTrue(verification.validMaxDistance(1));
        assertTrue(verification.validMaxDistance(999));
    }

    @Test
    public void checkIfMaxDistanceIncorrect(){
        assertFalse(verification.validMaxDistance(-1));
        assertFalse(verification.validMaxDistance(0));
        assertFalse(verification.validMaxDistance(1001));
    }

    @Test
    public void checkIfDurationCorrect(){
        assertTrue(verification.validDuration(10));
        assertTrue(verification.validDuration(1));
        assertTrue(verification.validDuration(99));
    }

    @Test
    public void checkIfDurationIncorrect(){
        assertFalse(verification.validDuration(-1));
        assertFalse(verification.validDuration(0));
        assertFalse(verification.validDuration(169));
    }
}
