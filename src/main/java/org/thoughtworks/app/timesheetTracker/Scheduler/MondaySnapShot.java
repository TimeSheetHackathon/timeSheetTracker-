package org.thoughtworks.app.timesheetTracker.Scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;
import java.util.List;

@Component
public class MondaySnapShot {
  @Autowired
  S3Client s3Client;
  private static final Logger log = LoggerFactory.getLogger(MondaySnapShot.class);

  @Scheduled(fixedRate = 5000)
  public void reportCurrentTime(){
    List<MissingTimeSheetData> timeSheetData = s3Client.getTimeSheetDataForProjectLastWeek();
    log.info(String.format("Size of time sheet data is %s", timeSheetData.size()));
  }
}
