package org.thoughtworks.app.timesheetTracker.DateUtil;


import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Calendar;

@Component
public class Date {
  public int getPreviousWeek() {
    return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 1;
  }

  public int getYearOfPreviousWeekStartingDate(ZonedDateTime today) {
    final ZonedDateTime startOfLastWeek = today.minusWeeks(1).with(DayOfWeek.MONDAY);
    return startOfLastWeek.getYear();
  }

}
