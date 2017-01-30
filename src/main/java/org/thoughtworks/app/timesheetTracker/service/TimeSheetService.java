package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCountForProject;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetPercentage;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.PeopleCounter;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static java.util.Collections.*;
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
                entry->entry.getWorkingLocation())
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetCount.builder()
                .workingLocation(cityEntry.getKey())
                .missingTimeSheetCount(valueOf(cityEntry.getValue()))
                .build())
                .collect(toList());
    }

    private Function<MissingTimeSheetData, Boolean> matchCountry(String country) {
        return timeSheetEntry ->  timeSheetEntry.getCountry().equals(country.toUpperCase());
    }

    public List<MissingTimeSheetPercentage> getMissingTimeSheetPercentagesForOfficesInCountry(String country) {
        return getMissingTimeSheet(s3Client.getTimeSheetFileForLastWeek(),
                matchCountry(country),
                entry->entry.getWorkingLocation())
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetPercentage.builder()
                .workingLocation(cityEntry.getKey())
                .missingTimeSheetPercentage(valueOf(calculatePercentage(cityEntry)))
                .build())
                .collect(toList());
    }

    public List<MissingTimeSheetCountForProject> getMissingTimeSheetForProjectsForOneCity(String city) {

        return getMissingTimeSheet(s3Client.getTimeSheetFileForProjectLastWeek(),
                matchCity(city),
                entry->entry.getProjectName())
                .entrySet().stream()
                .map(projectEntry -> MissingTimeSheetCountForProject.builder()
                .projectName(projectEntry.getKey())
                .missingTimeSheetCount(projectEntry.getValue())
                .build())
                .collect(toList());
    }

    private Function<MissingTimeSheetData, Boolean> matchCity(String city) {
        return timeSheetEntry ->  timeSheetEntry.getWorkingLocation().equals(city.toUpperCase());
    }

    private Map<String, Long> getMissingTimeSheet(List<MissingTimeSheetData> timeSheetFileForLastWeek,
                                                  Function<MissingTimeSheetData, Boolean> predicate,
                                                  Function<MissingTimeSheetData, String> classifier) {
        return timeSheetFileForLastWeek.stream()
                .collect(partitioningBy(timesheetEntry ->predicate.apply(timesheetEntry),
                        groupingBy(entry-> classifier.apply(entry), counting())))
                .get(true);
    }


    private Function<Map.Entry<String, Long>, Map<String, String>> getMissingTimeSheetCountMap
            (String key, Function<Map.Entry<String, Long>, String> l) {
        return cityEntry -> unmodifiableMap(Stream.of(
                new SimpleEntry<>(key, cityEntry.getKey()),
                new SimpleEntry<>("missingTimeSheet", l.apply(cityEntry))
                ).collect(Collectors.toMap(
                SimpleEntry::getKey,
                SimpleEntry::getValue)
                )
        );
    }

    private int calculatePercentage(Map.Entry<String, Long> cityEntry) {
        return cityEntry.getValue().intValue() * 100/peopleCounter.getPeopleCount().get(cityEntry.getKey());
    }
}

