package org.thoughtworks.app.timeSheetTracker.Scheduler;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timeSheetTracker.repository.MissingTimeSheetDataRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)

public class MondaySnapShotTest {
  private List<MissingTimeSheetData> timeSheetData;

  @Autowired
  private MissingTimeSheetDataRepository missingTimeSheetDataRepository;

  @Before
  public void setUp() throws Exception {
    timeSheetData  = Arrays.asList(
        MissingTimeSheetData.builder()
            .employeeId("1234")
            .employeeName("Hashan")
            .date(new DateTime(2016,11,6,0,0)).build(),
        MissingTimeSheetData.builder()
            .employeeId("1234")
            .employeeName("hosana")
            .date(new DateTime(2016,11,6,0,0)).build(),
        MissingTimeSheetData.builder()
            .employeeId("1234")
            .employeeName("makdad")
            .date(new DateTime(2016,11,6,0,0)).build(),
        MissingTimeSheetData.builder()
            .employeeId("1234")
            .employeeName("nazim")
            .date(new DateTime(2016,10,6,0,0)).build(),
        MissingTimeSheetData.builder()
            .employeeId("1234")
            .employeeName("current")
            .date(new DateTime(2017,2,7,0,0)).build()
    );

  }

  @Test
  public void shouldSaveDateInMongoDB() throws Exception {
    missingTimeSheetDataRepository.deleteAll();
    missingTimeSheetDataRepository.save(timeSheetData);
    assertEquals(5,missingTimeSheetDataRepository.findAll().size());

  }

  @Test
  public void shouldDeleteDataBeforeThreeMonths() throws Exception {
    missingTimeSheetDataRepository.deleteAll();
    missingTimeSheetDataRepository.save(timeSheetData);
    DateTime dateTime = new DateTime().minusMonths(2);

    List<MissingTimeSheetData> missingTimeSheetDataRemoved = missingTimeSheetDataRepository.removeByDate(dateTime);

    assertEquals(4, missingTimeSheetDataRemoved.size());

    assertEquals(1, missingTimeSheetDataRepository.findAll().size());

  }
}