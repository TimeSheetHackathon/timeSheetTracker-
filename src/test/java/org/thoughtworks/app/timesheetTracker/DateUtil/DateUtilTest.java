package org.thoughtworks.app.timesheetTracker.DateUtil;

import org.junit.Before;
import org.junit.Test;
import org.thoughtworks.app.timesheetTracker.Date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilTest {

  private DateUtil date;

  @Before
  public void setUp() throws Exception {
    date = new DateUtil();

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

  @Test
  public void testPassingFirstWeekDateShouldReturnLastWeekOfPreviousYear() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yy:mm:dd");
    Date actualDate = sdf.parse("17:01:01");
    assertEquals(53, date.getPreviousWeek(actualDate));
  }

  @Test
  public void testPassingDateShouldReturnPreviousWeek() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yy:mm:dd");
    Date actualDate = sdf.parse("17:01:10");
    assertEquals(1, date.getPreviousWeek(actualDate));
  }

}