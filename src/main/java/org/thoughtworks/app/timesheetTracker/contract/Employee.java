package org.thoughtworks.app.timesheetTracker.contract;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Employee {
    private String name;
    private Integer id;
}
