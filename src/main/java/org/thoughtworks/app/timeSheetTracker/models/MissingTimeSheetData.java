package org.thoughtworks.app.timeSheetTracker.models;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Data
@Builder
public class MissingTimeSheetData {
  @Id
  private String id;

  private String employeeId;
  private String workingLocation;
  private String country;
  private String projectName;
  private String employeeName;
  private String role;
  private int day;
  private int month;
  private int year;

  public Boolean validate() {
    return Stream.of(employeeId, employeeName)
        .allMatch(isNotEmpty().negate());
  }

  private Predicate<String> isNotEmpty() {
    return StringUtils::isEmpty;
  }
}


