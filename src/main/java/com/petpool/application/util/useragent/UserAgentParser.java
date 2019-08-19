package com.petpool.application.util.useragent;

public class UserAgentParser {

  private UserAgentParserStrategy strategy;
  public static final UserAgentParserResult DEFAULT_RESULT;

  static {

    DEFAULT_RESULT = new UserAgentParserResult(
        OS.builder().build(),
        Browser.builder().build());
  }

  public UserAgentParser(UserAgentParserStrategy strategy) {
    this.strategy = strategy;
  }

  public UserAgentParserResult parse(String userAgent) {
    UserAgentParserResult result = strategy.parse(userAgent);
    if (result == null) {
      return DEFAULT_RESULT;
    }
    return result;
  }
}
