package com.petpool.application.util.useragent;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class OS {

  private static final String UNKNOWN = "unknown";

  private String name;
  private String version;

  private OS(String name, String version) {
    this.name = name;
    this.version = version;
  }

  public static OSBuilder builder() {
    return new OSBuilder();
  }

  @Override
  public String toString() {
    return StringUtils.join(name, " ", version);
  }

  public static class OSBuilder {

    private String name = UNKNOWN;
    private String version = UNKNOWN;

    OSBuilder() {
    }

    public OSBuilder name(String name) {
      this.name = name;
      return this;
    }

    public OSBuilder version(String version) {
      this.version = version;
      return this;
    }

    public OS build() {
      if (name.equals(UNKNOWN)) {
        return new OS(UNKNOWN, UNKNOWN);
      }
      return new OS(name, version);
    }
  }
}
