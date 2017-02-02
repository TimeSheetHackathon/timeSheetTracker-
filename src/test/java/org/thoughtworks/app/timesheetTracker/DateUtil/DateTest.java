package org.thoughtworks.app.timesheetTracker.DateUtil;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class DateTest {

  private Date date;

  @Before
  public void setUp() throws Exception {
    date = new Date();

  }

  @Test
  public void shouldReturn2015AsYearForPreviousWeek() throws Exception {
    ZonedDateTime today = ZonedDateTime.of(2016, 1, 4, 0, 0, 0, 0, ZoneId.systemDefault());
    int prevWeekYear = date.getYearOfPreviousWeekStartingDate(today);
    assertEquals(2015, prevWeekYear);
  }

  @Test
  public void shouldReturn2016AsYearForPreviousWeek() throws Exception {
    ZonedDateTime today = ZonedDateTime.of(2016, 1, 11, 0, 0, 0, 0, ZoneId.systemDefault());
    int prevWeekYear = date.getYearOfPreviousWeekStartingDate(today);
    assertEquals(2016, prevWeekYear);
  }

}