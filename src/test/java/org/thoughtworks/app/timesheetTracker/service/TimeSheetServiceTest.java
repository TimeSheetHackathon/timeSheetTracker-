package org.thoughtworks.app.timesheetTracker.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.Arrays;
import java.util.Collections;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TimeSheetServiceTest {
    @Mock
    private S3Client client;

    @InjectMocks
    private TimeSheetService timeSheetService;

    @Test
    public void getMissingTimeSheetCountForIndiaOffices() throws Exception {
        final List<MissingTimeSheetData> result = Stream.of(

                Collections.unmodifiableMap(Stream.of(
                        new AbstractMap.SimpleEntry<>("id", "1"),
                        new AbstractMap.SimpleEntry<>("working-office", "bangalore"),
                        new AbstractMap.SimpleEntry<>("country", "India")
                        ).collect(Collectors.toMap(
                        aTimeSheetEntryForIndia -> aTimeSheetEntryForIndia.getKey(),
                        aTimeSheetEntryForIndia -> aTimeSheetEntryForIndia.getValue())
                        )
                ),
                Collections.unmodifiableMap(Stream.of(
                        new AbstractMap.SimpleEntry<>("id", "2"),
                        new AbstractMap.SimpleEntry<>("working-office", "pune"),
                        new AbstractMap.SimpleEntry<>("country", "India")
                        ).collect(Collectors.toMap(
                        aTimeSheetEntryForIndia -> aTimeSheetEntryForIndia.getKey(),
                        aTimeSheetEntryForIndia -> aTimeSheetEntryForIndia.getValue())
                        )
                ),
                Collections.unmodifiableMap(Stream.of(
                        new AbstractMap.SimpleEntry<>("id", "1"),
                        new AbstractMap.SimpleEntry<>("working-office", "sf"),
                        new AbstractMap.SimpleEntry<>("country", "US")
                        ).collect(Collectors.toMap(
                        aTimeSheetEntryForIndia -> aTimeSheetEntryForIndia.getKey(),
                        aTimeSheetEntryForIndia -> aTimeSheetEntryForIndia.getValue())
                        )
                )
        ).map(data -> new MissingTimeSheetData(data))
                .collect(Collectors.toList());
        when(client.getTimeSheetFileForLastWeek()).thenReturn(result);

        final List<Map<String, String>> serviceResult = timeSheetService.getMissingTimeSheetCountForIndiaOffices();

        assertEquals(2, serviceResult.size());

        final Map<String, List<Map<String, String>>> splitByCity =
                serviceResult.stream().collect(groupingBy(e -> e.get("workingLocation")));

        final List<Map<String, String>> bangalore = splitByCity.get("bangalore");
        assertEquals(1, bangalore.size());

        final List<Map<String, String>> pune = splitByCity.get("pune");
        assertEquals(1, pune.size());

    }

}