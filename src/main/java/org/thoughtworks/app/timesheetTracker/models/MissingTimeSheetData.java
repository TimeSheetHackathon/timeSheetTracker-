package org.thoughtworks.app.timesheetTracker.models;


import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Data
@Builder
public class MissingTimeSheetData {
	private String employeeId;
	private String workingLocation;
	private String country;
	private String projectName;
	private String employeeName;
	private String role;
	private Date date;

	public Boolean validate() {
		return Stream.of(employeeId, employeeName)
				.allMatch(isNotEmpty().negate());
	}
	private Predicate<String> isNotEmpty() {
		return StringUtils::isEmpty;
	}
}


