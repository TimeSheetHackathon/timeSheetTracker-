package org.thoughtworks.app.timeSheetTracker.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.thoughtworks.app.timeSheetTracker.service.TimeSheetService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TimeSheetTrackerController.class)
public class TimeSheetTrackerControllerTest {

    @MockBean
    private TimeSheetService timeSheetService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTotalTimeSheetMissingData() throws Exception {
        mockMvc.perform(get("/India/missingTimeSheetCounts"))
                .andExpect(status().isOk());

        verify(timeSheetService, times(1))
                .getMissingTimeSheetCountForOfficesInCountry("India");
    }

    @Test
    public void testTimeSheetPercentageMissing() throws Exception {
        mockMvc.perform(get("/India/missingTimeSheetPercentage"))
                .andExpect(status().isOk());

        verify(timeSheetService, times(1))
                .getMissingTimeSheetPercentagesForOfficesInCountry("India");
    }

    @Test
    public void testTimeSheetMissingByProjects() throws Exception {
        mockMvc.perform(get("/Bangalore/missingTimeSheetByProjects"))
                .andExpect(status().isOk());

        verify(timeSheetService, times(1))
                .getMissingTimeSheetForProjectsForOneCity("Bangalore");
    }

    @Test
    public void testPeopleWhoMissedTimeSheetForACity() throws Exception {
        mockMvc.perform(get("/city/Bangalore"))
                .andExpect(status().isOk());

        verify(timeSheetService, times(1)).getEmployeesNamesForACity("Bangalore");
    }

    @Test
    public void testTimeSheetMissingByEmployeesNames() throws Exception {
        mockMvc.perform(get("/city/Bangalore/project/Kroger/missingTimeSheetByEmployees"))
                .andExpect(status().isOk());

        verify(timeSheetService, times(1))
                .getEmployeesNamesForAProject("Bangalore","Kroger");

    }

    @Test
    public void testGetAllCountry() throws Exception {
        mockMvc.perform(get("/getAllCountry")).andExpect(status().isOk());

        verify(timeSheetService, times(1))
            .getCountries();
    }
}
