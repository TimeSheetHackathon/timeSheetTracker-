package org.thoughtworks.app.timeSheetTracker.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.thoughtworks.app.timeSheetTracker.models.Employee;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeopleCounterTest {

  @Mock
  private S3Client s3Client;

  private List<Employee> employees;

  @InjectMocks
  private PeopleCounter peopleCounter;

  @Before
  public void setUp() throws Exception {
    employees = Arrays.asList(
        Employee.builder()
            .country("INDIA")
            .workingLocation("BANGALORE")
            .employeeId("17168")
            .build(),
        Employee.builder()
            .country("INDIA")
            .workingLocation("GURGAON")
            .employeeId("17138")
            .build(),
        Employee.builder()
            .country("INDIA")
            .workingLocation("PUNE")
            .employeeId("18168")
            .build(),
        Employee.builder()
            .country("INDIA")
            .workingLocation("BANGALORE")
            .employeeId("17128")
            .build(),
        Employee.builder()
            .country("INDIA")
            .workingLocation("CHENNAI")
            .employeeId("17162")
            .build()
    );

  }

  @Test
  public void shouldReturnListOfTotalEmployeesGroupByCities() throws Exception {
    when(s3Client.getAllEmployees()).thenReturn(employees);

    Map<String, Long> peopleCount = peopleCounter.getPeopleCount("INDIA");

    assertEquals(4, peopleCount.size());
    assertEquals(new Long(2), peopleCount.get("BANGALORE"));
    assertEquals(new Long(1), peopleCount.get("PUNE"));
    assertEquals(new Long(1), peopleCount.get("CHENNAI"));
  }



}