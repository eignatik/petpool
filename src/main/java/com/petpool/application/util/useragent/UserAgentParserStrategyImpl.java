package com.petpool.application.util.useragent;

import static nl.basjes.parse.useragent.UserAgent.AGENT_NAME;
import static nl.basjes.parse.useragent.UserAgent.AGENT_VERSION;
import static nl.basjes.parse.useragent.UserAgent.OPERATING_SYSTEM_NAME;
import static nl.basjes.parse.useragent.UserAgent.OPERATING_SYSTEM_VERSION;

import com.petpool.application.util.useragent.Browser.BrowserBuilder;
import com.petpool.application.util.useragent.OS.OSBuilder;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class UserAgentParserStrategyImpl implements UserAgentParserStrategy {

  private UserAgentAnalyzer analyzer;
  private static final String HACKER = "Hacker";

  public UserAgentParserStrategyImpl() {
    analyzer = UserAgentAnalyzer
        .newBuilder()
        .build();
  }

  @Override
  public UserAgentParserResult parse(String userAgent) {
    UserAgent agent = analyzer.parse(userAgent);

    OSBuilder osBuilder = OS.builder();
    BrowserBuilder browserBuilder = Browser.builder();

    String osName = agent.getValue(OPERATING_SYSTEM_NAME);
    if (!osName.equals(HACKER) && !osName.equals("Unknown")) {
      osBuilder.name(osName);
    }

    String osVersion = agent.getValue(OPERATING_SYSTEM_VERSION);
    if (!osVersion.equals(HACKER) && !osName.equals("??")) {
      osBuilder.version(osVersion);
    }

    //Checking browserName.equals(osName) needed because lib is stupid.
    // Lib can return values equals OS'values, if browser description  not present in user-agent header.
    String browserName = agent.getValue(AGENT_NAME);
    if (!browserName.equals(HACKER) && !browserName.equals(osName)) {
      browserBuilder.name(browserName);
    }

    String browserVersion = agent.getValue(AGENT_VERSION);
    if (!browserVersion.equals(HACKER) && !browserName.equals(osVersion)) {
      browserBuilder.version(browserVersion);
    }

    return new UserAgentParserResult(osBuilder.build(), browserBuilder.build());
  }
}
