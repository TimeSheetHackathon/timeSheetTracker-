package org.thoughtworks.app.timesheetTracker.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;

public interface MissingTimeSheetDataRepository  extends MongoRepository<MissingTimeSheetData, String>{

}
