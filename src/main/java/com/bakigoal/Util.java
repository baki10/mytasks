package com.bakigoal;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class Util {

  private static final Locale LOCALE = new Locale("en");
  private static final TextStyle TEXT_STYLE = TextStyle.FULL;

  public static String getCurrentMonth() {
    LocalDate localDate = LocalDate.now();
    return getCurrentMonth(localDate);
  }

  public static String getCurrentMonth(LocalDate localDate) {
    return localDate.getMonth().getDisplayName(TEXT_STYLE, LOCALE);
  }

  public static String[] getMonths() {
    Month[] values = Month.values();
    String [] months = new String[12];
    for (int i = 0; i < values.length; i++) {
      Month month = values[i];
      months[i] =month.getDisplayName(TEXT_STYLE, LOCALE);
    }
    return months;
  }
}
