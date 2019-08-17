package com.petpool.application.util.useragent;

import static nl.basjes.parse.useragent.UserAgent.AGENT_NAME;
import static nl.basjes.parse.useragent.UserAgent.AGENT_VERSION;
import static nl.basjes.parse.useragent.UserAgent.OPERATING_SYSTEM_NAME;
import static nl.basjes.parse.useragent.UserAgent.OPERATING_SYSTEM_VERSION;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class UserAgentParserStrategyImpl implements UserAgentParserStrategy {

  private UserAgentAnalyzer analyzer;

  public UserAgentParserStrategyImpl() {
    analyzer = UserAgentAnalyzer
        .newBuilder()
        .build();
  }

  @Override
  public UserAgentParserResult parse(String userAgent) {
    UserAgent agent = analyzer.parse(userAgent);

    String osName = agent.getValue(OPERATING_SYSTEM_NAME);
    String osVersion = agent.getValue(OPERATING_SYSTEM_VERSION);
    String browserName = agent.getValue(AGENT_NAME);
    String browserVersion = agent.getValue(AGENT_VERSION);

    OS os = new OS(osName, osVersion);
    Browser browser = new Browser(browserName, browserVersion);
    return new UserAgentParserResult(os, browser);
  }
}
