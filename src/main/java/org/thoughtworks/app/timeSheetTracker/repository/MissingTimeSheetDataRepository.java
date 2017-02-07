package org.thoughtworks.app.timeSheetTracker.repository;


import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;

import java.util.List;

@Repository
public interface MissingTimeSheetDataRepository extends MongoRepository<MissingTimeSheetData, String> {
  @Query(value="{'date':{$lte:?0}}",delete=true)
  List<MissingTimeSheetData> removeByDate(DateTime date);
}
