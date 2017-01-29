package org.thoughtworks.app.timesheetTracker.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissingTimeSheetCount {
    private String workingLocation;
    private String missingTimeSheetCount;

}
