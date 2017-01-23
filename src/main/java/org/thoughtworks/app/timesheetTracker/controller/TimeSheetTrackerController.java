package org.thoughtworks.app.timesheetTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.app.timesheetTracker.service.TimeSheetService;

import java.util.List;
import java.util.Map;

@RestController
public class TimeSheetTrackerController {
 @Autowired
 TimeSheetService timeSheetService;

    @RequestMapping("/")
    public List<Map<String, String>> totalTimeSheetMissing() {
        return timeSheetService.getMissingTimeSheetCountForIndiaOffices();

    }
}
