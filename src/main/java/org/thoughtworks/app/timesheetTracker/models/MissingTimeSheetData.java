package org.thoughtworks.app.timesheetTracker.models;

import java.util.HashMap;
import java.util.Map;

public class MissingTimeSheetData {

	private String id;
	private String workingLocation;
	private String country;
	
	public MissingTimeSheetData(HashMap i) {
		id = (String) i.get("id");
		workingLocation = (String) i.get("working-office");
		country = (String) i.get("country");
	}

	public String getId() {
		return id;
	}

	public String getWorkingLocation() {
		return workingLocation;
	}

	public String getCountry() {
		return country;
	}
}
