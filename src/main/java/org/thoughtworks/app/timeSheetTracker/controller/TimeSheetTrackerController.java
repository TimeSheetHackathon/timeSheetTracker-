package org.thoughtworks.app.timeSheetTracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.app.timeSheetTracker.contract.*;
import org.thoughtworks.app.timeSheetTracker.service.TimeSheetService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
public class TimeSheetTrackerController {
    @Autowired
    private TimeSheetService timeSheetService;

    private final static Logger logger = LoggerFactory.getLogger(TimeSheetTrackerController.class);

    @RequestMapping("/{country}/missingTimeSheetCounts")
    public List<MissingTimeSheetCount> totalTimeSheetNumberMissing(@PathVariable("country") String country) throws UnsupportedEncodingException {
        logger.info(String.format("Getting missing time sheet data for %s", country));
        return timeSheetService.getMissingTimeSheetCountForOfficesInCountry(URLDecoder.decode(country, "UTF-8"));

    }
    
    @RequestMapping("/{country}/missingTimeSheetPercentage")
    public List<MissingTimeSheetPercentage> totalTimeSheetPercentageMissing(@PathVariable("country") String country) throws UnsupportedEncodingException {
        logger.info(String.format("Getting missing time sheet percentage data for %s", country));
        return timeSheetService.getMissingTimeSheetPercentagesForOfficesInCountry(URLDecoder.decode(country, "UTF-8"));
    }

    @RequestMapping("/{city}/missingTimeSheetByProjects")
    public List<MissingTimeSheetCountForProject> missingTimeSheetByProjects(@PathVariable("city") String city) throws UnsupportedEncodingException {
        logger.info(String.format("Getting missing time sheet data for %s", city));
        return timeSheetService.getMissingTimeSheetForProjectsForOneCity(URLDecoder.decode(city, "UTF-8"));
    }

    @RequestMapping("/city/{city}/project/{project}/missingTimeSheetByEmployees")
    public List<Employee> missingTimeSheetByEmployees(@PathVariable("city") String city, @PathVariable("project") String project) throws UnsupportedEncodingException {
        logger.info(String.format("Getting missing time sheet data for city %s and project %s", city, project));
        return timeSheetService.getEmployeesNamesForAProject(URLDecoder.decode(city, "UTF-8"), URLDecoder.decode(project, "UTF-8"));
    }

    @RequestMapping("/city/{city}")
    public List<Employee> peopleForACity(@PathVariable("city") String city) throws UnsupportedEncodingException {
        logger.info(String.format("Getting name of people who missed time sheet in %s", city));
        return timeSheetService.getEmployeesNamesForACity(URLDecoder.decode(city, "UTF-8"));
    }

    @RequestMapping("/getAllCountry")
    public List<MissingTimeSheetPercentage> getAllCountry(){
        return timeSheetService.getEntireTimeSheetMissingPercentage();
    }

}
