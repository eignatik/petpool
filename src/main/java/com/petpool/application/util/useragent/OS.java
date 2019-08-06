package com.petpool.application.util.useragent;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
public class OS {

  private String name;
  private String version;

  @Override
  public String toString() {
    return StringUtils.join(name, " ", version);
  }
}
