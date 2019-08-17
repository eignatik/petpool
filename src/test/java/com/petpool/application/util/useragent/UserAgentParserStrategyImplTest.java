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
        {"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0",
            new UserAgentParserResult(new OS("Windows NT", "8.1"),
                new Browser("Firefox", "36.0"))},
        {"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36",
            new UserAgentParserResult(new OS("Windows NT", "10.0"),
                new Browser("Chrome", "53.0.2785.116"))},
        {"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.101 Safari/537.36 OPR/40.0.2308.62",
            new UserAgentParserResult(new OS("Windows NT", "10.0"),
                new Browser("Opera", "40.0.2308.62"))},
        {"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            new UserAgentParserResult(new OS("Windows NT", "8"),
                new Browser("Safari", "5.1.7"))},
        {"Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; rv:11.0) like Gecko",
            new UserAgentParserResult(new OS("Windows NT", "10.0"),
                new Browser("Internet Explorer", "11.0"))},
        {"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            new UserAgentParserResult(new OS("Windows NT", "10.0"),
                new Browser("Edge", "25.10586"))},
        {"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            new UserAgentParserResult(new OS("Windows NT", "8"),
                new Browser("Safari", "5.1.7"))},
        {"Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25",
            new UserAgentParserResult(new OS("iOS", "6.0"),
                new Browser("Safari", "6.0"))},
        {"Mozilla/5.0 (Linux; U; Android 2.3.5; ru-ru; Philips W632 Build/GRJ90) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            new UserAgentParserResult(new OS("Android", "2.3.5"),
                new Browser("Stock Android Browser", "4.0"))}
    };
  }

  @Test(dataProvider = "parseUserAgent")
  public void testParse_(String userAgent, UserAgentParserResult expected) {
    UserAgentParserResult actual = parser.parse(userAgent);
    Assert.assertEquals(expected, actual);
  }
}
