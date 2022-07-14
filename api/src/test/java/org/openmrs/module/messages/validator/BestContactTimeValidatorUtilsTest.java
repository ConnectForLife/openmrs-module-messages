package org.openmrs.module.messages.validator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BestContactTimeValidatorUtilsTest {

  @Test
  public void isValidTime_whenTimeIsEmpty() {
    boolean actual = BestContactTimeValidatorUtils.isValidTime("");

    assertFalse(actual);
  }

  @Test
  public void isValidTime_whenTimeContainsLetters() {
    boolean actual = BestContactTimeValidatorUtils.isValidTime("testInvalidTime");

    assertFalse(actual);
  }

  @Test
  public void isValidTime_whenTimeContainsInvalidDigits() {
    boolean actual = BestContactTimeValidatorUtils.isValidTime("45:98");

    assertFalse(actual);
  }

  @Test
  public void isValidTime_whenTimeIsInProperFormat() {
    boolean actual = BestContactTimeValidatorUtils.isValidTime("16:20");

    assertTrue(actual);
  }
}
