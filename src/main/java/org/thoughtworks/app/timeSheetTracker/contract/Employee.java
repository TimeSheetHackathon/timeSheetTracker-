package org.thoughtworks.app.timeSheetTracker.contract;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Employee {
    private String name;
    private Integer id;
}
