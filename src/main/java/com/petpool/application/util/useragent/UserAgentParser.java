package com.petpool.application.util.useragent;

public class UserAgentParser {

  private UserAgentParserStrategy strategy;
  private static final UserAgentParserResult DEFAULT_RESULT = new UserAgentParserResult(
      new OS("unknown", "unknown"), new Browser("unknown", "unknown"));

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
