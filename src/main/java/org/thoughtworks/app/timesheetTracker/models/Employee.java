package org.thoughtworks.app.timesheetTracker.models;

import com.amazonaws.services.dynamodbv2.xspec.S;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {
  private String employeeId;
  private String employeeName;
  private String role;
  private String workingLocation;
  private String country;
}
