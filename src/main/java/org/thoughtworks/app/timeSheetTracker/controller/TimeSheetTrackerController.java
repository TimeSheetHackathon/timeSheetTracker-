package org.thoughtworks.app.timeSheetTracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.app.timeSheetTracker.contract.*;
import org.thoughtworks.app.timeSheetTracker.service.TimeSheetService;

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

    @RequestMapping("/city/{city}/project/{project}/missingTimeSheetByEmployees")
    public List<String> missingTimeSheetByEmployees(
            @PathVariable("city") String city, @PathVariable("project") String project) {
        logger.info(String.format("Getting missing time sheet data for city %s and project %s", city, project));
        return timeSheetService.getEmployeesNamesForAProject(city, project);
    }

    @RequestMapping("/city/{city}")
    public List<Employee> peopleForACity(@PathVariable("city") String city) {
        logger.info(String.format("Getting name of people who missed time sheet in %s", city));
        return timeSheetService.getEmployeesNamesForACity(city);
    }

    @RequestMapping("/getAllCountry")
    public List<Country> getAllCountry(){
        return timeSheetService.getCountries();
    }

}
