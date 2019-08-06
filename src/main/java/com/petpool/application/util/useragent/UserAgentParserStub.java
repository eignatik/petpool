package com.petpool.application.util.useragent;

public class UserAgentParserStub implements UserAgentParser {

  @Override
  public UserAgentParserResult parse(String userAgent) {
    return new UserAgentParserResult(new OS("windows", "10"), new Browser("firefox", "10"));
  }
}
