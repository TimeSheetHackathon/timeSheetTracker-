package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
public class TimeSheetService {

    @Autowired
    S3Client s3Client;

    public List<Map<String, String>> getMissingTimeSheetCountForIndiaOffices() {

        return s3Client.getTimeSheetFileForLastWeek().stream()
                .collect(partitioningBy(timeSheetEntry -> timeSheetEntry.getCountry().equals("India"),
                        groupingBy(MissingTimeSheetData::getWorkingLocation, counting())))
                .get(true)
                .entrySet().stream()
                .map(cityEntry -> Collections.unmodifiableMap(Stream.of(
                        new AbstractMap.SimpleEntry<>("workingLocation", cityEntry.getKey()),
                        new AbstractMap.SimpleEntry<>("numberOfMissingTimeSheet", String.valueOf(cityEntry.getValue()))
                        ).collect(Collectors.toMap(
                        missingTimeSheetEntryForIndianCity -> missingTimeSheetEntryForIndianCity.getKey(),
                        missingTimeSheetEntryForIndianCity -> missingTimeSheetEntryForIndianCity.getValue())
                        )
                ))
                .collect(toList());
    }

}

