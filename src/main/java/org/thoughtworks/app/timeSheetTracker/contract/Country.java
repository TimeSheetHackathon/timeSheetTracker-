package org.thoughtworks.app.timeSheetTracker.contract;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class Country {
  private String name;
  private List<String> cities;
}

