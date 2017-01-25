package org.thoughtworks.app.timesheetTracker.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissingTimeSheetData {
	private String employeeId;
	private String workingLocation;
	private String country;
	private String projectName;
}


