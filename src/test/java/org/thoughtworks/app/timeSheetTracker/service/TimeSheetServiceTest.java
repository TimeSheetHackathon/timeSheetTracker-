package org.thoughtworks.app.timeSheetTracker.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.thoughtworks.app.timeSheetTracker.contract.Employee;
import org.thoughtworks.app.timeSheetTracker.contract.MissingTimeSheetCount;
import org.thoughtworks.app.timeSheetTracker.contract.MissingTimeSheetCountForProject;
import org.thoughtworks.app.timeSheetTracker.contract.MissingTimeSheetPercentage;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timeSheetTracker.repository.PeopleCounter;
import org.thoughtworks.app.timeSheetTracker.repository.S3Client;

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

    private List<MissingTimeSheetData> result,duplicateData, differentIdEmployees;
    private Map employeeCount;
    private Map countries;

    @Before
    public void setUp() throws Exception {
        result = Arrays.asList(
                MissingTimeSheetData.builder()
                        .employeeId("1")
                        .employeeName("M,Gayathri")
                        .country("INDIA")
                        .workingLocation("BANGALORE")
                        .projectName("KROGER")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("2")
                        .employeeName("Sharma,Nishkarsh")
                        .country("INDIA")
                        .workingLocation("PUNE")
                        .projectName("DELTA")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("3")
                        .employeeName("Sao Paulo")
                        .country("US")
                        .workingLocation("sf")
                        .projectName("Cricket")
                        .build()
        );

        employeeCount = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>("BANGALORE", 2L),
                new AbstractMap.SimpleEntry<>("GURGAON", 2L),
                new AbstractMap.SimpleEntry<>("PUNE", 4L),
                new AbstractMap.SimpleEntry<>("CHENNAI", 5L),
                new AbstractMap.SimpleEntry<>("HYDERABAD", 1L)
        ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
        when(client.getTimeSheetDataForLastWeek()).thenReturn(result);


        duplicateData = Arrays.asList(
                MissingTimeSheetData.builder()
                        .employeeId("1")
                        .employeeName("M,Gayathri")
                        .country("INDIA")
                        .workingLocation("BANGALORE")
                        .projectName("KROGER")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("2")
                        .employeeName("Sharma,Nishkarsh")
                        .country("INDIA")
                        .workingLocation("PUNE")
                        .projectName("DELTA")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("1")
                        .employeeName("M,Gayathri")
                        .country("INDIA")
                        .workingLocation("BANGALORE")
                        .projectName("KROGER")
                        .build()
        );

        differentIdEmployees = Arrays.asList(
                MissingTimeSheetData.builder()
                        .employeeId("1")
                        .employeeName("M,Gayathri")
                        .country("INDIA")
                        .workingLocation("BANGALORE")
                        .projectName("KROGER")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("2")
                        .employeeName("Sharma,Nishkarsh")
                        .country("INDIA")
                        .workingLocation("PUNE")
                        .projectName("DELTA")
                        .build(),
                MissingTimeSheetData.builder()
                        .employeeId("3")
                        .employeeName("M,Gayathri")
                        .country("INDIA")
                        .workingLocation("BANGALORE")
                        .projectName("KROGER")
                        .build()
        );


        countries = new HashMap<>();
        countries.put("UK",new Long(3));
        countries.put("INDIA",new Long(4));
        countries.put("US",new Long(2));

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
        when(peopleCounter.getPeopleCount("INDIA")).thenReturn(employeeCount);

        final List<MissingTimeSheetPercentage> serviceResult =
                timeSheetService.getMissingTimeSheetPercentagesForOfficesInCountry("INDIA");

        assertEquals(5, serviceResult.size());

        final Map<String, List<MissingTimeSheetPercentage>> splitByCity =
                serviceResult.stream().collect(groupingBy(e -> e.getWorkingLocation()));

        final List<MissingTimeSheetPercentage> bangalore = splitByCity.get("BANGALORE");
        assertEquals(1, bangalore.size());
        assertEquals(Integer.valueOf(50), bangalore.get(0).getMissingTimeSheetPercentage());

        final List<MissingTimeSheetPercentage> pune = splitByCity.get("PUNE");
        assertEquals(Integer.valueOf(25), pune.get(0).getMissingTimeSheetPercentage());

        final List<MissingTimeSheetPercentage> gurgaon = splitByCity.get("GURGAON");
        assertEquals(Integer.valueOf(0), gurgaon.get(0).getMissingTimeSheetPercentage());
    }

    @Test
    public void testGetMissingTimeSheetForProjectsForOneCity() throws Exception {
       when(client.getTimeSheetDataForProjectLastWeek()).thenReturn(result);
        final List<MissingTimeSheetCountForProject> bangalore = timeSheetService
                .getMissingTimeSheetForProjectsForOneCity("Bangalore");

        assertEquals(1, bangalore.size());
        assertEquals("KROGER",bangalore.get(0).getProjectName());
        assertEquals(new Long(1), bangalore.get(0).getMissingTimeSheetCount());
    }

    @Test
    public void testGetEmployeesNamesForAProject() throws Exception {
        when(client.getTimeSheetDataForProjectLastWeek()).thenReturn(result);
        List<Employee> employees = timeSheetService.getEmployeesNamesForAProject("Bangalore", "Kroger");
        assertEquals(1, employees.size());
        assertEquals("M,Gayathri",employees.get(0).getName());
    }

    @Test
    public void shouldReturnUniqueEmployeesNamesForACity() throws Exception {
        when(client.getTimeSheetDataForProjectLastWeek()).thenReturn(duplicateData);
        List<Employee> employeesNames = timeSheetService.getEmployeesNamesForACity("Bangalore");
        assertEquals(1,employeesNames.size());
        assertEquals("M,Gayathri",employeesNames.get(0).getName());
        assertEquals(new Integer(1), employeesNames.get(0).getId());
    }

    @Test
    public void shouldReturnSameEmployeesNamesWhenEmployeesHaveDifferentId() throws Exception {
        when(client.getTimeSheetDataForProjectLastWeek()).thenReturn(differentIdEmployees);
        List<Employee> employeesNames = timeSheetService.getEmployeesNamesForACity("Bangalore");
        assertEquals(2, employeesNames.size());
        assertEquals("M,Gayathri",employeesNames.get(0).getName());
        assertEquals(new Integer(1), employeesNames.get(0).getId());

        assertEquals("M,Gayathri", employeesNames.get(1).getName());
        assertEquals(new Integer(3), employeesNames.get(1).getId());

    }

    @Test
    public void testGetEntireTimeSheetMissingPercentage(){
        when(client.getTimeSheetDataForLastWeek()).thenReturn(result);
        when(peopleCounter.getAllPeopleCountCountryWise()).thenReturn(countries);
        List<MissingTimeSheetPercentage> countryPercentage = timeSheetService.getEntireTimeSheetMissingPercentage();
        assertEquals(3, countryPercentage.size());
        assertEquals("UK", countryPercentage.get(0).getWorkingLocation());
        assertEquals(new Integer(0),countryPercentage.get(0).getMissingTimeSheetPercentage());
        assertEquals("INDIA", countryPercentage.get(1).getWorkingLocation());
        assertEquals(new Integer(50), countryPercentage.get(1).getMissingTimeSheetPercentage());

    }
}
