package org.thoughtworks.app.timesheetTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.app.timesheetTracker.service.TimeSheetService;

import java.util.List;
import java.util.Map;

@RestController
public class TimeSheetTrackerController {
    @Autowired
    private TimeSheetService timeSheetService;

    @RequestMapping("/{country}/missingTimeSheetCounts")
    public List<Map<String, String>> totalTimeSheetNumberMissing(@PathVariable("country") String country) {
        return timeSheetService.getMissingTimeSheetCountForOfficesInCountry(country);

    }
    
    @RequestMapping("/{country}/missingTimeSheetPercentage")
    public List<Map<String, String>> totalTimeSheetPercentageMissing(@PathVariable("country") String country) {
        return timeSheetService.getMissingTimeSheetPercentagesForOfficesInCountry(country);
    }

    @RequestMapping("/{city}/missingTimeSheetByProjects")
    public List<Map<String, String>> missingTimeSheetByProjects(@PathVariable("city") String city) {
        return timeSheetService.getMissingTimeSheetForProjectsForOneCity(city);
    }


}
