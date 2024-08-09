package com.esentri.tracing.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Extension {

  public String id;
  public String name;
  public String shortName;
  public List<String> keywords;
}
