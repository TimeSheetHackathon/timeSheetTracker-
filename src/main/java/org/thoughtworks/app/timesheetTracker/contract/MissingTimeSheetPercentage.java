package org.thoughtworks.app.timesheetTracker.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissingTimeSheetPercentage {
    private String workingLocation;
    private String missingTimeSheetPercentage;
}
