package org.thoughtworks.app.timesheetTracker.contract;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MissingTimeSheetCountForProject {
    private String projectName;
    private Long missingTimeSheetCount;

}
