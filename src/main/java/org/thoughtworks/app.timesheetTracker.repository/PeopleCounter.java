package org.thoughtworks.app.timesheetTracker.repository;

import org.springframework.stereotype.Repository;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class PeopleCounter {
    private Map<String, Integer> peopleCount = Collections.unmodifiableMap(Stream.of(
            new SimpleEntry<>("BANGALORE", 385),
            new SimpleEntry<>("GURGAON", 136),
            new SimpleEntry<>("PUNE", 284),
            new SimpleEntry<>("CHENNAI", 187),
            new SimpleEntry<>("HYDERABAD", 135),
            new SimpleEntry<>("COIMBATORE", 15)
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));


    public Map<String, Integer> getPeopleCount() {
        return peopleCount;
    }

}
