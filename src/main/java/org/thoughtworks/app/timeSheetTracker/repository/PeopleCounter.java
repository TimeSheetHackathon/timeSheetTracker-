package org.thoughtworks.app.timeSheetTracker.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thoughtworks.app.timeSheetTracker.models.Employee;

import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

@Repository
public class PeopleCounter {
  @Autowired
  private S3Client s3Client;

  private final static Logger logger = LoggerFactory.getLogger(PeopleCounter.class);

  public Map<String, Long> getPeopleCount(String country) {
    Map<String, Long> employeeByCity = s3Client.getAllEmployees().stream()
        .distinct()
        .collect(partitioningBy(employee -> matchCountry(country).test(employee),
            groupingBy(Employee::getWorkingLocation, counting())))
        .get(true);
    logger.info(String.format("employee count  by city for the country %s is %s", country, employeeByCity));

    return employeeByCity;
  }

  private Predicate<Employee> matchCountry(String country) {
    return employee -> employee.getCountry().equals(country.toUpperCase());
  }
}
