package com.petpool.application.util.useragent;

import static nl.basjes.parse.useragent.UserAgent.AGENT_NAME;
import static nl.basjes.parse.useragent.UserAgent.AGENT_VERSION;
import static nl.basjes.parse.useragent.UserAgent.OPERATING_SYSTEM_NAME;
import static nl.basjes.parse.useragent.UserAgent.OPERATING_SYSTEM_VERSION;
import static nl.basjes.parse.useragent.UserAgent.UNKNOWN_VALUE;
import static nl.basjes.parse.useragent.UserAgent.UNKNOWN_VERSION;

import java.util.Objects;
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

    String tmp = agent.getValue(OPERATING_SYSTEM_NAME);

    String osName = Objects.equals(tmp, UNKNOWN_VALUE) ? "unknown" : tmp;
    tmp = agent.getValue(OPERATING_SYSTEM_VERSION);
    String osVersion = Objects.equals(tmp, UNKNOWN_VERSION) ? "unknown" : tmp;
    String browserName = agent.getValue(AGENT_NAME);
    String browserVersion = agent.getValue(AGENT_VERSION);

    if (Objects.equals(osName, browserName) && !Objects.equals(osName, "Hacker")) {
      browserName = "unknown";
      browserVersion = "unknown";
    }

    OS os = new OS(osName, osVersion);
    Browser browser = new Browser(browserName, browserVersion);
    return new UserAgentParserResult(os, browser);
  }
}
