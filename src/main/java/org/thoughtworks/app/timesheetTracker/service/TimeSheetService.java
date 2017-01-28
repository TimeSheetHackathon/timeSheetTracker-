package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetPercentage;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.PeopleCounter;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

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
        return getMissingTimeSheetForCountry(s3Client.getTimeSheetFileForLastWeek(), country)
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetCount.builder()
                .workingLocation(cityEntry.getKey())
                .missingTimeSheetCount(valueOf(cityEntry.getValue()))
                .build())
                .collect(toList());
    }

    public List<MissingTimeSheetPercentage> getMissingTimeSheetPercentagesForOfficesInCountry(String country) {
        return getMissingTimeSheetForCountry(s3Client.getTimeSheetFileForLastWeek(), country)
                .entrySet().stream()
                .map(cityEntry -> MissingTimeSheetPercentage.builder()
                .workingLocation(cityEntry.getKey())
                .missingTimeSheetPercentage(valueOf(calculatePercentage(cityEntry)))
                .build())
                .collect(toList());
    }

    private Map<String, Long> getMissingTimeSheetForCountry(List<MissingTimeSheetData> timeSheetFileForLastWeek,
                                                       String country) {
        return timeSheetFileForLastWeek.stream()
                .collect(partitioningBy(timeSheetEntry ->  timeSheetEntry.getCountry().equals(country.toUpperCase()),
                        groupingBy(MissingTimeSheetData::getWorkingLocation, counting())))
                .get(true);
    }

    private int calculatePercentage(Map.Entry<String, Long> cityEntry) {
        return cityEntry.getValue().intValue() * 100/peopleCounter.getPeopleCount().get(cityEntry.getKey());
    }
}

