package com.petpool.application.util.useragent;

public class UserAgentParserStrategyStub implements UserAgentParserStrategy {

  @Override
  public UserAgentParserResult parse(String userAgent) {
    return new UserAgentParserResult(new OS("windows", "10"), new Browser("firefox", "10"));
  }
}
