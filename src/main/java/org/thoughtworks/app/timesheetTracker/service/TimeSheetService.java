package org.thoughtworks.app.timesheetTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

/**
 * Created by deeptim on 1/23/17.
 */
@Service
public class TimeSheetService {

    @Autowired
    S3Client s3Client;

    public List<HashMap<String, String>> getTimeSheetFileForLastWeek(){
        List<MissingTimeSheetData> missingTimeSheetDataList =  s3Client.getTimeSheetFileForLastWeek();

        return missingTimeSheetDataList.stream().collect(partitioningBy(e -> e.getCountry().equals("India"),
                groupingBy(MissingTimeSheetData::getWorkingLocation, counting())))
                .get(true)
                .entrySet().stream()
                .map(cityEntry -> {
                    HashMap<String, String> result = new HashMap<>();
                    result.put("workingLocation", cityEntry.getKey());
                    result.put("numberOfMissingTimeSheet", String.valueOf(cityEntry.getValue()));
                    return result;
                })
                .collect(Collectors.toList());
    }

}

