package org.thoughtworks.app.timesheetTracker.Date;


import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtil {

  public int getPreviousWeek(Date date) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.WEEK_OF_YEAR, -1);
    return calendar.get(Calendar.WEEK_OF_YEAR);
  }

  public int getYearOfPreviousWeekStartingDate(ZonedDateTime date) {
    final ZonedDateTime startOfLastWeek = date.minusWeeks(1).with(DayOfWeek.MONDAY);
    return startOfLastWeek.getYear();
  }



}
