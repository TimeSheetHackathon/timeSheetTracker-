package org.thoughtworks.app.timeSheetTracker.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissingTimeSheetPercentage {
    private String workingLocation;
    private Integer missingTimeSheetPercentage;
}
