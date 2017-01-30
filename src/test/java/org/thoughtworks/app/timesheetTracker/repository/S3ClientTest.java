package org.thoughtworks.app.timesheetTracker.repository;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class S3ClientTest {

    @Mock
    private Environment environment;
    @Mock
    private S3Object s3Object;
    @Mock
    private AmazonS3Client amazonS3Client;
    @Mock
    private S3ObjectInputStream s3ObjectInputStream;
    @Mock
    private HttpGet httpGet;

    @InjectMocks
    private S3Client s3Client;


    @Test
    public void testS3ClientGetTimeSheetFileForLastWeek() throws Exception {

        InputStream targetStream = this.getClass().getClassLoader().getResourceAsStream("aws.json");

        when(environment.getProperty("cloud.aws.weekly.timesheet.file.prefix")).thenReturn("prefix");
        when(environment.getProperty("cloud.aws.timesheet.bucket.name")).thenReturn("bucket-name");

        S3ObjectInputStream returnObject = new S3ObjectInputStream(targetStream, httpGet);
        when(amazonS3Client.getObject("bucket-name", "prefix")).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(returnObject);

        List<MissingTimeSheetData> timeSheetFileForLastWeek = s3Client.getTimeSheetFileForLastWeek();
        assertEquals(6, timeSheetFileForLastWeek.size());
        long indiaanMissingTimeSheet =
                timeSheetFileForLastWeek.stream().filter(x -> x.getCountry().equals("INDIA")).count();

        assertEquals(4, indiaanMissingTimeSheet);
        assertEquals(2, timeSheetFileForLastWeek.stream().filter(x -> x.getWorkingLocation().equals("BANGALORE")).count());

    }
}
