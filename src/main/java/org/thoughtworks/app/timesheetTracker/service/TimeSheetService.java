package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetPercentage;
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

    public List<MissingTimeSheetCount> getMissingTimeSheetCountForOfficesInCountry(String country) {
        return getIndiaMissingTimeSheet(s3Client.getTimeSheetFileForLastWeek(), getMissingTimeSheetsForCountry(country))
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetCount.builder()
                .workingLocation(cityEntry.getKey())
                .missingTimeSheetCount(valueOf(cityEntry.getValue()))
                .build())
                .collect(toList());
    }

    public List<MissingTimeSheetPercentage> getMissingTimeSheetPercentagesForOfficesInCountry(String country) {
        return getIndiaMissingTimeSheet(s3Client.getTimeSheetFileForLastWeek(), getMissingTimeSheetsForCountry(country))
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetPercentage.builder()
                .workingLocation(cityEntry.getKey())
                .missingTimeSheetPercentage(valueOf(calculatePercentage(cityEntry)))
                .build())
                .collect(toList());
    }

    public List<Map<String, String>> getMissingTimeSheetForProjectsForOneCity(String city) {

        return getIndiaMissingTimeSheet(s3Client.getTimeSheetFileForProjectLastWeek(),
                x-> x.getWorkingLocation().equals(city.toUpperCase()))
                .entrySet().stream()
                .map(getMissingTimeSheetCountMap("projectName", x->valueOf(x.getValue())))
                .collect(toList());
    }

    private Function<MissingTimeSheetData, Boolean> getMissingTimeSheetsForCountry(String country) {
        return x -> x.getCountry().equals(country.toUpperCase());
    }

    private Function<Map.Entry<String, Long>, Map<String, String>>
    getMissingTimeSheetCountMap(String key, Function<Map.Entry<String, Long>, String> l) {
        return cityEntry -> Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>(key, cityEntry.getKey()),
                new SimpleEntry<>("missingTimeSheet", l.apply(cityEntry))
                ).collect(Collectors.toMap(
                SimpleEntry::getKey,
                SimpleEntry::getValue)
                )
        );
    }

    private Map<String, Long> getIndiaMissingTimeSheet(List<MissingTimeSheetData> timeSheetFileForLastWeek,
                                                       Function<MissingTimeSheetData, Boolean> predicate) {
        return timeSheetFileForLastWeek.stream()
                .collect(partitioningBy(timeSheetEntry -> predicate.apply(timeSheetEntry),
                        groupingBy(MissingTimeSheetData::getWorkingLocation, counting())))
                .get(true);
    }

    private int calculatePercentage(Map.Entry<String, Long> cityEntry) {
        return cityEntry.getValue().intValue() * 100/peopleCounter.getPeopleCount().get(cityEntry.getKey());
    }
}

