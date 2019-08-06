package com.petpool.application.util.useragent;

import org.apache.commons.lang3.StringUtils;

public class OS {

  private String name;
  private String version;

  public OS(String name, String version) {
    this.name = name.isEmpty()?"unknown":name;
    this.version = version.isEmpty()?"unknown":version;
  }

  @Override
  public String toString() {
    return StringUtils.join(name, " ", version);
  }
}
