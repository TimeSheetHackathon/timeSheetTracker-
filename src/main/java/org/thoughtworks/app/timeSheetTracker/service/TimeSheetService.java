package org.thoughtworks.app.timeSheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timeSheetTracker.contract.Employee;
import org.thoughtworks.app.timeSheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timeSheetTracker.contract.MissingTimeSheetCountForProject;
import org.thoughtworks.app.timeSheetTracker.contract.MissingTimeSheetPercentage;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timeSheetTracker.repository.PeopleCounter;
import org.thoughtworks.app.timeSheetTracker.repository.S3Client;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.*;

@Service
public class TimeSheetService {

  @Autowired
  private S3Client s3Client;

  @Autowired
  private PeopleCounter peopleCounter;


  public List<MissingTimeSheetCount> getMissingTimeSheetCountForOfficesInCountry(String country) {
    return getMissingTimeSheet(s3Client.getTimeSheetDataForLastWeek(),
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
    Map<String, Long> cityWithMissingTimeSheetCount =
        getMissingTimeSheet(s3Client.getTimeSheetDataForLastWeek(),
            matchCountry(country),
            MissingTimeSheetData::getWorkingLocation);
    return peopleCounter.getPeopleCount(country)
        .keySet().stream()
        .map(city -> MissingTimeSheetPercentage.builder()
            .workingLocation(city)
            .missingTimeSheetPercentage(calculatePercentage(cityWithMissingTimeSheetCount, city, country))
            .build())
        .sorted(Comparator.comparingInt(MissingTimeSheetPercentage::getMissingTimeSheetPercentage))
        .collect(toList());
  }

  public List<MissingTimeSheetCountForProject> getMissingTimeSheetForProjectsForOneCity(String city) {
    return getMissingTimeSheet(s3Client.getTimeSheetDataForProjectLastWeek(),
        matchCity(city),
        MissingTimeSheetData::getProjectName)
        .entrySet().stream()
        .map(projectEntry -> MissingTimeSheetCountForProject.builder()
            .projectName(projectEntry.getKey())
            .city(city)
            .missingTimeSheetCount(projectEntry.getValue())
            .build())
        .collect(toList());
  }

  public List<Employee> getEmployeesNamesForAProject(String city, String project) {
     return s3Client.getTimeSheetDataForProjectLastWeek().stream()
            .filter(matchCity(city).and(matchProject(project)))
            .map(missingTimeSheetData -> Employee.builder()
                    .name(missingTimeSheetData.getEmployeeName())
                    .id(Integer.valueOf(missingTimeSheetData.getEmployeeId()))
                    .build())
            .collect(toList());
  }

  public List<Employee> getEmployeesNamesForACity(String city) {
    return s3Client.getTimeSheetDataForProjectLastWeek().stream()
        .filter(matchCity(city))
        .map(timeSheetData -> Employee.builder()
            .name(timeSheetData.getEmployeeName())
            .id(new Integer(timeSheetData.getEmployeeId()))
            .build())
        .distinct()
        .collect(toList());
  }

  public List<MissingTimeSheetPercentage> getEntireTimeSheetMissingPercentage() {
    List<MissingTimeSheetData> timeSheetDataForLastWeek = s3Client.getTimeSheetDataForLastWeek();
    Map<String, Long> countryWithMissingTimeSheetCount =
        timeSheetDataForLastWeek.stream().collect(groupingBy(MissingTimeSheetData::getCountry, counting()));

    return peopleCounter.getAllPeopleCountCountryWise()
        .keySet().stream()
        .map(country->MissingTimeSheetPercentage.builder()
        .workingLocation(country)
        .missingTimeSheetPercentage(calculatePercentageForCountry(countryWithMissingTimeSheetCount, country))
        .build())
        .filter(isNULLString().negate())
        .sorted(Comparator.comparingInt(MissingTimeSheetPercentage::getMissingTimeSheetPercentage))
        .collect(toList());
  }

  private Predicate<MissingTimeSheetPercentage> isNULLString() {
    return missingTimeSheetPercentage -> missingTimeSheetPercentage.getWorkingLocation().toUpperCase().equals("NULL");
  }

  private Predicate<MissingTimeSheetData> matchCity(String city) {
    return timeSheetEntry -> timeSheetEntry.getWorkingLocation().equals(city.toUpperCase());
  }

  private Predicate<MissingTimeSheetData> matchProject(String project) {
    return timeSheetEntry -> timeSheetEntry.getProjectName().equals(project.toUpperCase());
  }

  private Predicate<MissingTimeSheetData> matchCountry(String country) {
    return timeSheetEntry -> timeSheetEntry.getCountry().equals(country.toUpperCase());
  }

  private Map<String, Long> getMissingTimeSheet(List<MissingTimeSheetData> timeSheetFileForLastWeek,
                                                Predicate<MissingTimeSheetData> predicate,
                                                Function<MissingTimeSheetData, String> classifier) {
    return timeSheetFileForLastWeek.stream()
        .collect(partitioningBy(predicate,
            groupingBy(classifier, counting())))
        .get(true);
  }

  private Integer calculatePercentage(Map<String, Long> cityWithMissingTimeSheetCount, String city, String country) {
    Long missingTimeSheetCount = cityWithMissingTimeSheetCount.get(city);
    return missingTimeSheetCount == null ? 0 :
        missingTimeSheetCount.intValue() * 100 / peopleCounter.getPeopleCount(country).get(city).intValue();
  }

  private Integer calculatePercentageForCountry(Map<String, Long> missingTimeSheetCountForCountries, String country) {
    Long missingTimeSheetCount = missingTimeSheetCountForCountries.get(country);
    return missingTimeSheetCount == null ? 0 :
        missingTimeSheetCount.intValue() * 100 / peopleCounter.getAllPeopleCountCountryWise().get(country).intValue();
  }


}

