package org.thoughtworks.app.timesheetTracker.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.thoughtworks.app.timesheetTracker.service.TimeSheetService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TimeSheetTrackerController.class)
public class TimeSheetTrackerControllerTest {

    @MockBean
    TimeSheetService timeSheetService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTotalTimeSheetMissingData() throws Exception {
        mockMvc.perform(get("/timeSheetNumberMissing"))
                .andExpect(status().isOk());

        verify(timeSheetService, times(1)).getMissingTimeSheetCountForIndiaOffices();
    }

    @Test
    public void testTimeSheetPercentageMissing() throws Exception {
        mockMvc.perform(get("/timeSheetPercentageMissing"))
                .andExpect(status().isOk());

        verify(timeSheetService, times(1)).getMissingTimeSheetPercentagesForIndiaOffices();
    }
}
