package org.thoughtworks.app.timesheetTracker.repository;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.thoughtworks.app.timesheetTracker.controller.TimeSheetTrackerController;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;


@Repository

public class S3Client {

  @Autowired
  private Environment env;

  @Autowired
  private AmazonS3Client amazonS3Client;

  private final static Logger logger = LoggerFactory.getLogger(TimeSheetTrackerController.class);

  @Cacheable("timeSheetFileForLastWeek")
  public List<MissingTimeSheetData> getTimeSheetFileForLastWeek() {
    final String filePrefix = env.getProperty("cloud.aws.weekly.timesheet.file.prefix");
    return fetchFileFromAWS().andThen(parseEmployeeData()).apply(filePrefix);
  }

  @Cacheable("employeesNamesOfMissingTimeSheet")
  public List<MissingTimeSheetData> getTimeSheetFileForProjectLastWeek() {
    final String filePrefix =
        env.getProperty("cloud.aws.weekly.project.timeseet.mising.file.prefix");
    return fetchFileFromAWS().andThen(parseEmployeeData()).apply(filePrefix);
  }

  private Function<String, S3ObjectInputStream> fetchFileFromAWS() {
    return filePrefix -> {
      logger.info("Fetching file from aws");

      final S3Object s3Object =
          amazonS3Client.getObject(env.getProperty("cloud.aws.timesheet.bucket.name"),
              String.format(filePrefix, getPreviousWeek()));
      try {
        return s3Object.getObjectContent();
      } catch (Exception e) {
        logger.info("Fetching file failed from aws");
        logger.info(e.getMessage());
      }
      return null;
    };
  }


  private Function<InputStream, List<MissingTimeSheetData>> parseEmployeeData() {
    return input -> {
      final ObjectMapper mapper = new ObjectMapper();
      try {
        return new JSONArray(IOUtils.toString(input)).toList().stream()
            .map(e -> {
              Map timeSheetDataMap = mapper.convertValue(e, Map.class);

              return MissingTimeSheetData.builder()
                  .country(valueOf(timeSheetDataMap.getOrDefault("country", "")).toUpperCase())
                  .employeeId(valueOf(timeSheetDataMap.getOrDefault("id", "")).toUpperCase())
                  .workingLocation(valueOf(timeSheetDataMap.getOrDefault("working-office", "")).toUpperCase())
                  .projectName(valueOf(timeSheetDataMap.getOrDefault("project-name", "")).toUpperCase())
                  .employeeName(valueOf(timeSheetDataMap.getOrDefault("name", "")))
                  .build();
            })
            .collect(Collectors.toList());
      } catch (IOException e) {
        logger.info(e.getMessage());
      }
      return Collections.emptyList();
    };
  }

  private int getPreviousWeek() {
    return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 1;
  }

}

