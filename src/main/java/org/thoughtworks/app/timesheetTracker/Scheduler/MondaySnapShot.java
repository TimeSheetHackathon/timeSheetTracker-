package org.thoughtworks.app.timesheetTracker.Scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.MissingTimeSheetDataRepository;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.Date;
import java.util.List;

@Component
public class MondaySnapShot {
  @Autowired
  private S3Client s3Client;
  private static final Logger log = LoggerFactory.getLogger(MondaySnapShot.class);

  @Autowired
  private MissingTimeSheetDataRepository missingTimeSheetDataRepository;

  @Scheduled(cron = "0 20 12 * * MON")
  public void reportCurrentTime(){
    List<MissingTimeSheetData> timeSheetData = s3Client.getTimeSheetDataForProjectLastWeek();
    log.info(String.format("Size of time sheet data is %s", timeSheetData.size()));
    missingTimeSheetDataRepository.save(timeSheetData);
  }
}
