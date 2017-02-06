package org.thoughtworks.app.timesheetTracker.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thoughtworks.app.timesheetTracker.models.MissingTimeSheetData;

@Repository
public interface MissingTimeSheetDataRepository extends MongoRepository<MissingTimeSheetData, String> {

}
