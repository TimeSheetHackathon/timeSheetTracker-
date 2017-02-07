package org.thoughtworks.app.timeSheetTracker.Scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timeSheetTracker.repository.MissingTimeSheetDataRepository;
import org.thoughtworks.app.timeSheetTracker.repository.S3Client;

import java.util.Date;
import java.util.List;

@Component
public class MondaySnapShot {
  @Autowired
  private S3Client s3Client;
  private static final Logger logger = LoggerFactory.getLogger(MondaySnapShot.class);

  @Autowired
  private MissingTimeSheetDataRepository missingTimeSheetDataRepository;

  @Scheduled(cron = "0 0 1 * * TUE")
  public void reportCurrentTime(){
    List<MissingTimeSheetData> timeSheetData = s3Client.getTimeSheetDataForProjectLastWeek();
    logger.info(String.format("Size of time sheet data is %s", timeSheetData.size()));
    missingTimeSheetDataRepository.save(timeSheetData);
  }

  @Scheduled(cron ="0 0 1 * * SUN-MON")
  public void cleanUpOldData() {
    System.out.println("Jeeeeep");
    Date date = new Date();
    logger.info(String.valueOf(date));
    List<MissingTimeSheetData> byDayMonthYear = missingTimeSheetDataRepository.findByDayMonthYear(7, 2, 2017);
    logger.debug(String.valueOf(byDayMonthYear));
    missingTimeSheetDataRepository.delete(byDayMonthYear);
  }

}
