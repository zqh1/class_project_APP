package ca.dal.csci3130.quickcash.jobmanagement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class JobVerificationJUnitTest {

    private static JobVerification jobVer;

    @BeforeClass
    public static void startJobVerification() {
        jobVer = new JobVerification();
    }

    @Test
    public void checkIfTitleCorrect() {
        assertTrue(jobVer.validTitle("Clean a pc"));
        assertTrue(jobVer.validTitle("Cook supper"));
        assertTrue(jobVer.validTitle("Move heavy furniture"));
    }

    @Test
    public void checkIfTitleIncorrect() {
        assertFalse(jobVer.validTitle(""));
        assertFalse(jobVer.validTitle("12345678"));
        assertFalse(jobVer.validTitle("     "));
    }

    @Test
    public void checkIfDateCorrect() {
        assertTrue(jobVer.validDate(2022, 4, 5, 22, 30));
        assertTrue(jobVer.validDate(2022, 3, 1, 0, 0));
        assertTrue(jobVer.validDate(2022, 2, 28, 23, 59));
    }

    @Test
    public void checkIfDateIncorrect() {
        assertFalse(jobVer.validDate(9999, 4, 5, 22, 30));
        assertFalse(jobVer.validDate(2000, 4, 5, 22, 30));
        assertFalse(jobVer.validDate(2020, 2, 31, 0, 30));
    }

    @Test
    public void checkIfDurationCorrect() {
        assertTrue(jobVer.validDuration(10));
        assertTrue(jobVer.validDuration(40));
        assertTrue(jobVer.validDuration(80));
    }

    @Test
    public void checkIfDurationIncorrect() {
        assertFalse(jobVer.validDuration(0));
        assertFalse(jobVer.validDuration(100));
        assertFalse(jobVer.validDuration(-10));
    }

    @Test
    public void checkISalaryCorrect() {
        assertTrue(jobVer.validSalary(20));
        assertTrue(jobVer.validSalary(500));
        assertTrue(jobVer.validSalary(10000));
    }

    @Test
    public void checkISalaryIncorrect() {
        assertFalse(jobVer.validSalary(0));
        assertFalse(jobVer.validSalary(-1));
    }

}
