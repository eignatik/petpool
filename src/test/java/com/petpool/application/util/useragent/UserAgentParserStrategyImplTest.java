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
            new UserAgentParserResult(
                OS.builder().name("Android").version("7.0").build(),
                Browser.builder().name("Chrome").version("53.0.2785.124").build())},

        {"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36 OPR/62.0.3331.99",
            new UserAgentParserResult(
                OS.builder().name("Windows NT").version("10.0").build(),
                Browser.builder().name("Opera").version("62.0.3331.99").build())},

        {"something", UserAgentParser.DEFAULT_RESULT},

        {null, UserAgentParser.DEFAULT_RESULT},

        {"(Windows NT 10.0)  Chrome/53.0.2785.101",
            new UserAgentParserResult(
                OS.builder().name("Windows NT").version("10.0").build(),
                Browser.builder().name("Chrome").version("53.0.2785.101").build())},

        {"Safari/534.57.2",
            new UserAgentParserResult(
                OS.builder().build(),
                Browser.builder().name("Safari").version("534.57.2").build())},

        {"Windows NT 6.1",
            new UserAgentParserResult(
                OS.builder().name("Windows NT").version("7").build(),
                Browser.builder().build())}
    };

  }

  @Test(dataProvider = "parseUserAgent")
  public void test_Parse_UserAgent(String userAgent, UserAgentParserResult expected) {
    UserAgentParserResult actual = parser.parse(userAgent);
    Assert.assertEquals(actual, expected);
  }
}
