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
    }

    @Test
    public void checkIfDescriptionCorrect() {
        assertTrue(jobVer.validDescription("Clean a pc and change a hard drive"));
        assertTrue(jobVer.validDescription("Cook supper on weekends"));
        assertTrue(jobVer.validDescription("Help to transport som heavy furniture"));
    }

    @Test
    public void checkIfDescriptionIncorrect() {
        assertFalse(jobVer.validDescription(""));
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
        assertFalse(jobVer.validSalary(9999999));
    }

}
