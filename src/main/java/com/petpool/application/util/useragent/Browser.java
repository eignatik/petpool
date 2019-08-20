package com.petpool.application.util.useragent;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Browser {

  private static final String UNKNOWN = "unknown";

  private String name;
  private String version;

  private Browser(String name, String version) {
    this.name = name;
    this.version = version;
  }

  public static BrowserBuilder builder() {
    return new BrowserBuilder();
  }

  @Override
  public String toString() {
    return StringUtils.join(name, " ", version);
  }

  public static class BrowserBuilder {

    private String name = UNKNOWN;
    private String version = UNKNOWN;

    BrowserBuilder() {
    }

    public BrowserBuilder name(String name) {
      this.name = name;
      return this;
    }

    public BrowserBuilder version(String version) {
      this.version = version;
      return this;
    }

    public Browser build() {
      if (name.equals(UNKNOWN)) {
        return new Browser(UNKNOWN, UNKNOWN);
      }
      return new Browser(name, version);
    }

  }
}
