package org.thoughtworks.app.timesheetTracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCountForProject;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetPercentage;
import org.thoughtworks.app.timesheetTracker.service.TimeSheetService;

import java.util.List;

@RestController
public class TimeSheetTrackerController {
    @Autowired
    private TimeSheetService timeSheetService;

    private final static Logger logger = LoggerFactory.getLogger(TimeSheetTrackerController.class);

    @RequestMapping("/{country}/missingTimeSheetCounts")
    public List<MissingTimeSheetCount> totalTimeSheetNumberMissing(@PathVariable("country") String country) {
        logger.info(String.format("Getting missing time sheet data for %s", country));
        return timeSheetService.getMissingTimeSheetCountForOfficesInCountry(country);

    }
    
    @RequestMapping("/{country}/missingTimeSheetPercentage")
    public List<MissingTimeSheetPercentage> totalTimeSheetPercentageMissing(@PathVariable("country") String country) {
        logger.info(String.format("Getting missing time sheet percentage data for %s", country));
        return timeSheetService.getMissingTimeSheetPercentagesForOfficesInCountry(country);
    }

    @RequestMapping("/{city}/missingTimeSheetByProjects")
    public List<MissingTimeSheetCountForProject> missingTimeSheetByProjects(@PathVariable("city") String city) {
        logger.info(String.format("Getting missing time sheet data for %s", city));
        return timeSheetService.getMissingTimeSheetForProjectsForOneCity(city);
    }


}
