package org.thoughtworks.app.timeSheetTracker.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.thoughtworks.app.timeSheetTracker.models.MissingTimeSheetData;

import java.util.List;

@Repository
public interface MissingTimeSheetDataRepository extends MongoRepository<MissingTimeSheetData, String> {
  @Query("{ 'day' : ?0 , 'month' : ?1, 'year' : ?2}")
  List<MissingTimeSheetData> findByDayMonthYear(int day, int month, int year);

}
