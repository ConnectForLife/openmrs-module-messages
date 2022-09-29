package org.openmrs.module.messages.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BestContactTimeValidatorUtils {

  private static final String BEST_CONTACT_TIME_REGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

  public static boolean isValidTime(String time) {
    if (time == null) {
      return false;
    }

    Pattern pattern = Pattern.compile(BEST_CONTACT_TIME_REGEX);
    Matcher matcher = pattern.matcher(time);

    return matcher.matches();
  }

  private BestContactTimeValidatorUtils() {}
}
