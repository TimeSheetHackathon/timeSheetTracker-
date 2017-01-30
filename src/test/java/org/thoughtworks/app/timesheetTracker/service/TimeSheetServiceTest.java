package org.thoughtworks.app.timesheetTracker.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetCountForProject;
import org.thoughtworks.app.timesheetTracker.contract.MissingTimeSheetPercentage;
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
    public void testGetMissingTimeSheetCountForIndiaOffices() throws Exception {
        final List<MissingTimeSheetCount> serviceResult =
                timeSheetService.getMissingTimeSheetCountForOfficesInCountry("India");

        assertEquals(2, serviceResult.size());


        final Map<String, List<MissingTimeSheetCount>> splitByCity =
                serviceResult.stream().collect(groupingBy(MissingTimeSheetCount::getWorkingLocation));

        final List<MissingTimeSheetCount> bangalore = splitByCity.get("BANGALORE");
        assertEquals(1, bangalore.size());

        final List<MissingTimeSheetCount> pune = splitByCity.get("PUNE");
        assertEquals(1, pune.size());

    }

    @Test
    public void testGetMissingTimeSheetPercentagesForIndiaOffices() throws Exception {
        when(peopleCounter.getPeopleCount()).thenReturn(employeeCount);

        final List<MissingTimeSheetPercentage> serviceResult =
                timeSheetService.getMissingTimeSheetPercentagesForOfficesInCountry("INDIA");

        assertEquals(2, serviceResult.size());

        final Map<String, List<MissingTimeSheetPercentage>> splitByCity =
                serviceResult.stream().collect(groupingBy(e -> e.getWorkingLocation()));

        final List<MissingTimeSheetPercentage> bangalore = splitByCity.get("BANGALORE");
        assertEquals(1, bangalore.size());
        assertEquals(Integer.valueOf(50), bangalore.get(0).getMissingTimeSheetPercentage());

        final List<MissingTimeSheetPercentage> pune = splitByCity.get("PUNE");
        assertEquals(Integer.valueOf(25), pune.get(0).getMissingTimeSheetPercentage());
    }

    @Test
    public void testGetMissingTimeSheetForProjectsForOneCity() throws Exception {
       when(client.getTimeSheetFileForProjectLastWeek()).thenReturn(result);
        final List<MissingTimeSheetCountForProject> bangalore = timeSheetService
                .getMissingTimeSheetForProjectsForOneCity("Bangalore");

        assertEquals(1, bangalore.size());
        assertEquals("KROGER",bangalore.get(0).getProjectName());
        assertEquals(new Long(1), bangalore.get(0).getMissingTimeSheetCount());
    }
}
