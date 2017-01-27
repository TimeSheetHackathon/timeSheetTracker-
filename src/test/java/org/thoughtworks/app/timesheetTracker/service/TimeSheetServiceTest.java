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
    private Map employeeCount;

    @Before
    public void setUp() throws Exception {
        result = Arrays.asList(
                MissingTimeSheetData.builder()
                        .employeeId("1")
                        .country("INDIA")
                        .workingLocation("BANGALORE")
                        .projectName("KROGER")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("2")
                        .country("INDIA")
                        .workingLocation("PUNE")
                        .projectName("DELTA")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("3")
                        .country("US")
                        .workingLocation("sf")
                        .projectName("Cricket")
                        .build()
        );

        employeeCount = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>("BANGALORE", 2),
                new AbstractMap.SimpleEntry<>("GURGAON", 2),
                new AbstractMap.SimpleEntry<>("PUNE", 4),
                new AbstractMap.SimpleEntry<>("CHENNAI", 5),
                new AbstractMap.SimpleEntry<>("HYDERABAD", 1)
        ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
        when(client.getTimeSheetFileForLastWeek()).thenReturn(result);
    }

    @Test
    public void getMissingTimeSheetCountForIndiaOffices() throws Exception {
        final List<Map<String, String>> serviceResult =
                timeSheetService.getMissingTimeSheetCountForOfficesInCountry("India");

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
        when(peopleCounter.getPeopleCount()).thenReturn(employeeCount);

        final List<Map<String, String>> serviceResult =
                timeSheetService.getMissingTimeSheetPercentagesForOfficesInCountry("INDIA");

        assertEquals(2, serviceResult.size());

        final Map<String, List<Map<String, String>>> splitByCity =
                serviceResult.stream().collect(groupingBy(e -> e.get("workingLocation")));

        final List<Map<String, String>> bangalore = splitByCity.get("BANGALORE");
        assertEquals(1, bangalore.size());
        assertEquals("50", bangalore.get(0).get("missingTimeSheet"));

        final List<Map<String, String>> pune = splitByCity.get("PUNE");
        assertEquals("25", pune.get(0).get("missingTimeSheet"));
    }

    @Test
    public void getMissingTimeSheetForProjectCityWise() throws Exception {
        when(client.getTimeSheetFileForProjectLastWeek()).thenReturn(result);
        List<Map<String, String>> missingTimeSheet = timeSheetService.getMissingTimeSheetForProjectsForOneCity("Bangalore");
        assertEquals(1, missingTimeSheet.size());
    }
}
