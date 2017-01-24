package org.thoughtworks.app.timesheetTracker.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.PeopleCounter;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TimeSheetServiceTest {

    @Mock
    private S3Client client;

    @Mock
    private PeopleCounter peopleCounter;

    @InjectMocks
    private TimeSheetService timeSheetService;

    private List<MissingTimeSheetData> result;

    @Before
    public void setUp() throws Exception {
        result = Arrays.asList(
                MissingTimeSheetData.builder()
                        .employeeId("1")
                        .country("India")
                        .workingLocation("BANGALORE")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("2")
                        .country("India")
                        .workingLocation("PUNE")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("3")
                        .country("US")
                        .workingLocation("sf")
                        .build()
        );
    }

    @Test
    public void getMissingTimeSheetCountForIndiaOffices() throws Exception {
        when(client.getTimeSheetFileForLastWeek()).thenReturn(result);

        final List<Map<String, String>> serviceResult = timeSheetService.getMissingTimeSheetCountForIndiaOffices();

        assertEquals(2, serviceResult.size());

        final Map<String, List<Map<String, String>>> splitByCity =
                serviceResult.stream().collect(groupingBy(e -> e.get("workingLocation")));

        final List<Map<String, String>> bangalore = splitByCity.get("BANGALORE");
        assertEquals(1, bangalore.size());

        final List<Map<String, String>> pune = splitByCity.get("PUNE");
        assertEquals(1, pune.size());

    }

    @Test
    public void getMissingTimeSheetPercentagesForIndiaOffices() throws Exception {
        Map employeeCount = getPeopleCounter();
        when(client.getTimeSheetFileForLastWeek()).thenReturn(result);
        when(peopleCounter.getPeopleCount()).thenReturn(employeeCount);

        final List<Map<String, String>> serviceResult = timeSheetService.getMissingTimeSheetPercentagesForIndiaOffices();

        assertEquals(2, serviceResult.size());

        final Map<String, List<Map<String, String>>> splitByCity =
                serviceResult.stream().collect(groupingBy(e -> e.get("workingLocation")));

        final List<Map<String, String>> bangalore = splitByCity.get("BANGALORE");
        assertEquals(1, bangalore.size());
        assertEquals("50", bangalore.get(0).get("numberOfMissingTimeSheet"));

        final List<Map<String, String>> pune = splitByCity.get("PUNE");
        assertEquals("25", pune.get(0).get("numberOfMissingTimeSheet"));
    }


    private Map getPeopleCounter() {
        return Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>("BANGALORE", 2),
                new SimpleEntry<>("GURGAON", 2),
                new SimpleEntry<>("PUNE", 4),
                new SimpleEntry<>("CHENNAI", 5),
                new SimpleEntry<>("HYDERABAD", 1)
        ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
    }

}