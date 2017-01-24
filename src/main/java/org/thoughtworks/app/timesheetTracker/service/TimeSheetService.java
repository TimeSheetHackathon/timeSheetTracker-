package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import static java.util.stream.Collectors.*;

@Service
public class TimeSheetService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private PeopleCounter peopleCounter;

    @Autowired
    private Environment environment;

    public List<Map<String, String>> getMissingTimeSheetCountForIndiaOffices() {
        return getIndiaMissingTimeSheet()
                .entrySet().stream()
                .map(getEntryMapFunction(x-> String.valueOf(x.getValue())))
                .collect(toList());
    }

    public List<Map<String, String>> getMissingTimeSheetPercentagesForIndiaOffices() {
        return getIndiaMissingTimeSheet()
                .entrySet().stream()
                .map(getEntryMapFunction(x-> String.valueOf(calculatePercentage(x))))
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

    private Map<String, Long> getIndiaMissingTimeSheet() {
        return s3Client.getTimeSheetFileForLastWeek().stream()
                .collect(partitioningBy(timeSheetEntry -> timeSheetEntry.getCountry().equals("India"),
                        groupingBy(MissingTimeSheetData::getWorkingLocation, counting())))
                .get(true);
    }

    private int calculatePercentage(Map.Entry<String, Long> cityEntry) {
        return cityEntry.getValue().intValue() * 100/peopleCounter.getPeopleCount().get(cityEntry.getKey());
    }
}

