package org.thoughtworks.app.timesheetTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;
import org.thoughtworks.app.timesheetTracker.service.TimeSheetService;

import java.util.HashMap;
import java.util.List;

/**
 * Created by deeptim on 1/23/17.
 */
@RestController
public class TimeSheetTrackerController {
 @Autowired
 TimeSheetService timeSheetService;

    @RequestMapping("/")
    @ResponseBody
    public List<HashMap<String, String>> totalTimeSheetMissing() {
        return timeSheetService.getTimeSheetFileForLastWeek();

    }
}
