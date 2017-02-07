package org.thoughtworks.app.timeSheetTracker.Scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timeSheetTracker.repository.MissingTimeSheetDataRepository;
import org.thoughtworks.app.timeSheetTracker.repository.S3Client;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MondaySnapShotTest {
  @Mock
  private S3Client s3Client;
  @Mock
  private MissingTimeSheetDataRepository missingTimeSheetDataRepository;

  @InjectMocks
  private MondaySnapShot mondaySnapShot;
  @Test
  public void reportCurrentTime() throws Exception {
    List<MissingTimeSheetData> missingTimeSheetData = Collections.singletonList(MissingTimeSheetData.builder().build());
    when(s3Client.getTimeSheetDataForProjectLastWeek()).thenReturn(missingTimeSheetData);
    mondaySnapShot.reportCurrentTime();
    verify(missingTimeSheetDataRepository, times(1)).save(missingTimeSheetData);
  }

}