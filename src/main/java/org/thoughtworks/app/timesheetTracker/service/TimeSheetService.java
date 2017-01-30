package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timesheetTracker.contract.Employee;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCountForProject;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetPercentage;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.PeopleCounter;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.*;

@Service
public class TimeSheetService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private PeopleCounter peopleCounter;

    public List<MissingTimeSheetCount> getMissingTimeSheetCountForOfficesInCountry(String country) {
        return getMissingTimeSheet(s3Client.getTimeSheetFileForLastWeek(),
                matchCountry(country),
                MissingTimeSheetData::getWorkingLocation)
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetCount.builder()
                        .workingLocation(cityEntry.getKey())
                        .missingTimeSheetCount(valueOf(cityEntry.getValue()))
                        .build())
                .collect(toList());
    }


    public List<MissingTimeSheetPercentage> getMissingTimeSheetPercentagesForOfficesInCountry(String country) {
        return getMissingTimeSheet(s3Client.getTimeSheetFileForLastWeek(),
                matchCountry(country),
                MissingTimeSheetData::getWorkingLocation)
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetPercentage.builder()
                        .workingLocation(cityEntry.getKey())
                        .missingTimeSheetPercentage(calculatePercentage(cityEntry))
                        .build())
                .sorted(Comparator.comparingInt(MissingTimeSheetPercentage::getMissingTimeSheetPercentage))
                .collect(toList());
    }

    public List<MissingTimeSheetCountForProject> getMissingTimeSheetForProjectsForOneCity(String city) {

        return getMissingTimeSheet(s3Client.getTimeSheetFileForProjectLastWeek(),
                matchCity(city),
                MissingTimeSheetData::getProjectName)
                .entrySet().stream()
                .map(projectEntry -> MissingTimeSheetCountForProject.builder()
                        .projectName(projectEntry.getKey())
                        .missingTimeSheetCount(projectEntry.getValue())
                        .build())
                .collect(toList());
    }

    public List<String> getEmployeesNamesForAProject(String city,String project){
       return s3Client.getTimeSheetFileForProjectLastWeek().stream()
               .filter(timeSheetData -> timeSheetData.getWorkingLocation().equals(city.toUpperCase()) &&
               timeSheetData.getProjectName().equals(project.toUpperCase()))
               .map(MissingTimeSheetData::getEmployeeName)
               .collect(toList());
    }

    private Function<MissingTimeSheetData, Boolean> matchCity(String city) {
        return timeSheetEntry -> timeSheetEntry.getWorkingLocation().equals(city.toUpperCase());
    }

    private Function<MissingTimeSheetData, Boolean> matchCountry(String country) {
        return timeSheetEntry -> timeSheetEntry.getCountry().equals(country.toUpperCase());
    }

    private Map<String, Long> getMissingTimeSheet(List<MissingTimeSheetData> timeSheetFileForLastWeek,
                                                  Function<MissingTimeSheetData, Boolean> predicate,
                                                  Function<MissingTimeSheetData, String> classifier) {
        return timeSheetFileForLastWeek.stream()
                .collect(partitioningBy(predicate::apply,
                        groupingBy(classifier, counting())))
                .get(true);
    }

    private int calculatePercentage(Map.Entry<String, Long> cityEntry) {
        return cityEntry.getValue().intValue() * 100 / peopleCounter.getPeopleCount().get(cityEntry.getKey());
    }

    public List<Employee> getEmployeesNamesForACity(String city) {
        return s3Client.getTimeSheetFileForProjectLastWeek().stream()
                .filter(timeSheetData -> timeSheetData.getWorkingLocation().equals(city.toUpperCase()))
                .map(timeSheetData -> Employee.builder()
                        .name(timeSheetData.getEmployeeName())
                        .id(new Integer(timeSheetData.getEmployeeId()))
                        .build())
                .distinct()
                .collect(toList());
    }
}

