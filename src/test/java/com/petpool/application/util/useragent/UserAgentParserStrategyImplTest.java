package com.petpool.application.util.useragent;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserAgentParserStrategyImplTest {

  private static UserAgentParserStrategy parser = new UserAgentParserStrategyImpl();

  @DataProvider
  public static Object[][] parseUserAgent() {
    return new Object[][]{
        {"Mozilla/5.0 (Linux; Android 7.0; Nexus 6 Build/NBD90Z) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.124 Mobile Safari/537.36",
            new UserAgentParserResult(new OS("Android", "7.0"),
                new Browser("Chrome", "53.0.2785.124"))},
        {"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36 OPR/62.0.3331.99",
            new UserAgentParserResult(new OS("Windows NT", "10.0"),
                new Browser("Opera", "62.0.3331.99"))},
        {"something",
            new UserAgentParserResult(new OS("Hacker", "Hacker"),
                new Browser("Hacker", "Hacker"))},
        {null,
            new UserAgentParserResult(new OS("Hacker", "Hacker"),
                new Browser("Hacker", "Hacker"))},
        {"(Windows NT 10.0)  Chrome/53.0.2785.101",
            new UserAgentParserResult(new OS("Windows NT", "10.0"),
                new Browser("Chrome", "53.0.2785.101"))},
        {"Safari/534.57.2",
            new UserAgentParserResult(new OS("unknown", "unknown"),
                new Browser("Safari", "534.57.2"))},
        {"Windows NT 6.1",
            new UserAgentParserResult(new OS("Windows NT", "7"),
                new Browser("unknown", "unknown"))}
    };
  }

  @Test(dataProvider = "parseUserAgent")
  public void testParse_(String userAgent, UserAgentParserResult expected) {
    UserAgentParserResult actual = parser.parse(userAgent);
    Assert.assertEquals(expected, actual);
  }
}
