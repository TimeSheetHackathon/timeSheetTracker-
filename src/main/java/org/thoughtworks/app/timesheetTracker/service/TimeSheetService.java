package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.PeopleCounter;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.*;

@Service
public class TimeSheetService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private PeopleCounter peopleCounter;

    public List<Map<String, String>> getMissingTimeSheetCountForIndiaOffices() {
        return getIndiaMissingTimeSheet(s3Client.getTimeSheetFileForLastWeek())
                .entrySet().stream()
                .map(getEntryMapFunction(x-> valueOf(x.getValue())))
                .collect(toList());
    }

    public List<Map<String, String>> getMissingTimeSheetPercentagesForIndiaOffices() {
        return getIndiaMissingTimeSheet(s3Client.getTimeSheetFileForLastWeek())
                .entrySet().stream()
                .map(getEntryMapFunction(x-> valueOf(calculatePercentage(x))))
                .collect(toList());
    }

    public List<Map<String, String>> getMissingTimeSheetForProjects() {
         return s3Client.getTimeSheetFileForProjectLastWeek().stream()
                .collect(partitioningBy(timeSheetEntry -> timeSheetEntry.getCountry().equals("INDIA"),
                        groupingBy(MissingTimeSheetData::getProjectName, counting())))
                .get(true)
                .entrySet().stream()
                .map(projectEntry -> Collections.unmodifiableMap(Stream.of(
                        new SimpleEntry<>("projectName", projectEntry.getKey()),
                        new SimpleEntry<>("numberOfMissingTimeSheet", valueOf(projectEntry.getValue()))
                ).collect(Collectors.toMap(
                        SimpleEntry::getKey,
                        SimpleEntry::getValue)
                )))
         .collect(toList());
    }

    private Function<Map.Entry<String, Long>, Map<String, String>> getEntryMapFunction(Function<Map.Entry<String, Long>, String> l) {
        return cityEntry -> Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>("workingLocation", cityEntry.getKey()),
                new SimpleEntry<>("numberOfMissingTimeSheet", l.apply(cityEntry))
                ).collect(Collectors.toMap(
                SimpleEntry::getKey,
                SimpleEntry::getValue)
                )
        );
    }

    private Map<String, Long> getIndiaMissingTimeSheet(List<MissingTimeSheetData> timeSheetFileForLastWeek) {
        return timeSheetFileForLastWeek.stream()
                .collect(partitioningBy(timeSheetEntry -> timeSheetEntry.getCountry().equals("INDIA"),
                        groupingBy(MissingTimeSheetData::getWorkingLocation, counting())))
                .get(true);
    }

    private int calculatePercentage(Map.Entry<String, Long> cityEntry) {
        return cityEntry.getValue().intValue() * 100/peopleCounter.getPeopleCount().get(cityEntry.getKey());
    }
}

