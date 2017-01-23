package org.thoughtworks.app.timesheetTracker;

import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;
import org.thoughtworks.app.timesheetTracker.repository.S3Client;;

@SpringBootApplication
@ComponentScan(basePackages = { "org.thoughtworks.app.timesheetTracker.*" })
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
