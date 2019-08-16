package com.petpool.application.util.useragent;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UserAgentParserStrategyImplTest {

  private static UserAgentParserStrategy parser = new UserAgentParserStrategyImpl();

  @Test
  public void testParse() {
    // Chrome 53:
    String userAgent1 = "Mozilla/5.0 (Linux; Android 7.0; Nexus 6 Build/NBD90Z) AppleWebKit/537"
        + ".36 (KHTML, like Gecko) Chrome/53.0.2785.124 Mobile Safari/537.36";

    // Opera 62:
    String userAgent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, "
        + "like Gecko) Chrome/75.0.3770.100 Safari/537.36 OPR/62.0.3331.99";

    // Mozilla Firefox 36:
    String userAgent3 = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0";

    // Google Chrome 53 (Win 10 x64):
    String userAgent4 = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like "
        + "Gecko) Chrome/53.0.2785.116 Safari/537.36";

    // Opera 40 (Win 10 x64):
    String userAgent5 = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like "
        + "Gecko) Chrome/53.0.2785.101 Safari/537.36 OPR/40.0.2308.62";

    // Apple Safari 5.1 (Win 8 x64):
    String userAgent6 = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/534.57.2 (KHTML, like "
        + "Gecko) Version/5.1.7 Safari/534.57.2";

    // Internet Explorer 11 (Win 10 x64):
    String userAgent7 = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E;"
        + " rv:11.0) like Gecko";

    // Microsoft Edge (Win 10 x64):
    String userAgent8 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like"
        + " Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586";

    // Apple Safari 5.1 (Win 8 x64):
    String userAgent9 = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/534.57.2 (KHTML, like"
        + " Gecko) Version/5.1.7 Safari/534.57.2";

    // iPhone:
    String userAgent10 = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26"
        + " (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25";

    // Android 2.3.5
    String userAgent11 = "Mozilla/5.0 (Linux; U; Android 2.3.5; ru-ru; Philips W632 "
        + "Build/GRJ90) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

    //-----------------------------------------------------------------------------------------

    Assert.assertEquals(parser.parse(userAgent1).toString(),
        "UserAgentParserResult(os=Android 7.0, browser=Chrome 53.0.2785.124)");
    Assert.assertEquals(parser.parse(userAgent2).toString(),
        "UserAgentParserResult(os=Windows NT 10.0, browser=Opera 62.0.3331.99)");
    Assert.assertEquals(parser.parse(userAgent3).toString(),
        "UserAgentParserResult(os=Windows NT 8.1, browser=Firefox 36.0)");
    Assert.assertEquals(parser.parse(userAgent4).toString(),
        "UserAgentParserResult(os=Windows NT 10.0, browser=Chrome 53.0.2785.116)");
    Assert.assertEquals(parser.parse(userAgent5).toString(),
        "UserAgentParserResult(os=Windows NT 10.0, browser=Opera 40.0.2308.62)");
    Assert.assertEquals(parser.parse(userAgent6).toString(),
        "UserAgentParserResult(os=Windows NT 8, browser=Safari 5.1.7)");
    Assert.assertEquals(parser.parse(userAgent7).toString(),
        "UserAgentParserResult(os=Windows NT 10.0, browser=Internet Explorer 11.0)");
    Assert.assertEquals(parser.parse(userAgent8).toString(),
        "UserAgentParserResult(os=Windows NT 10.0, browser=Edge 25.10586)");
    Assert.assertEquals(parser.parse(userAgent9).toString(),
        "UserAgentParserResult(os=Windows NT 8, browser=Safari 5.1.7)");
    Assert.assertEquals(parser.parse(userAgent10).toString(),
        "UserAgentParserResult(os=iOS 6.0, browser=Safari 6.0)");
    Assert.assertEquals(parser.parse(userAgent11).toString(),
        "UserAgentParserResult(os=Android 2.3.5, browser=Stock Android Browser 4.0)");
  }
}
