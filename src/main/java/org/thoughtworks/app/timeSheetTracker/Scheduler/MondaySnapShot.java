package org.thoughtworks.app.timeSheetTracker.Scheduler;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timeSheetTracker.repository.MissingTimeSheetDataRepository;
import org.thoughtworks.app.timeSheetTracker.repository.S3Client;

import java.util.List;

@Component

public class MondaySnapShot {
  @Autowired
  private S3Client s3Client;
  private static final Logger logger = LoggerFactory.getLogger(MondaySnapShot.class);

  @Autowired
  private MissingTimeSheetDataRepository missingTimeSheetDataRepository;

  @Scheduled(cron="0 0 2 ? * TUE *")
  public void reportCurrentTime(){
    List<MissingTimeSheetData> timeSheetData =
        s3Client.getTimeSheetDataForProjectLastWeek();
    logger.info(String.format("Size of time sheet data is %s", timeSheetData.size()));
    missingTimeSheetDataRepository.save(timeSheetData);
  }

  @Scheduled(cron ="0 0 1 1/1 * ? *")
  public void cleanUpOldData() {
    DateTime date = new DateTime().minusMonths(2);
    List<MissingTimeSheetData> missingTimeSheetDatas =
        missingTimeSheetDataRepository.removeByDate(date);
    logger.debug(String.valueOf(missingTimeSheetDatas));
  }

}
