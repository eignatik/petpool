package com.petpool.application.util.ip;

import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;

public class IpParserTest {
    private IpParserImpl impl = new IpParserImpl();
    private final static String DEFAULT_PARSE_VALUE = "";

    @Test
    public void parserForHeaderThenLocalHost() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("localhost", 5050);
        headers.setHost(address);
        String ip = impl.parse(headers);
        Assert.assertEquals(ip, "localhost");
    }

    @Test
    public void parserForHeaderThenHaveIp() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("10.0.0.1", 8080);
        headers.setHost(address);
        String ip = impl.parse(headers);
        Assert.assertEquals(ip, "10.0.0.1");
    }

    @Test
    public void parserForHeaderThenEmpty() {
        HttpHeaders headers = new HttpHeaders();
        String ip = impl.parse(headers);
        Assert.assertEquals(ip, DEFAULT_PARSE_VALUE);
    }

    @Test
    public void parserForHeaderThenNull() {
        HttpHeaders headers = null;
        String ip = impl.parse(headers);
        Assert.assertEquals(ip, DEFAULT_PARSE_VALUE);
    }

    @Test
    public void parseForHeaderWithHttpXForwardedFor() {
        HttpHeaders headers = new HttpHeaders();
        String rightIp = "120.10.7.9";
        headers.set("HTTP_X_FORWARDED_FOR", rightIp);
        String ip = impl.parse(headers);
        Assert.assertEquals(ip, rightIp);
    }

    @Test
    public void parseForHeaderWithHttpXForwardedForThenIpSeparated() {
        HttpHeaders headers = new HttpHeaders();
        String rightIp = "120.10.7.9";
        headers.set("HTTP_X_FORWARDED_FOR", "120.10.7.9, 10.0.0.1");
        String ip = impl.parse(headers);
        Assert.assertEquals(ip, rightIp);
    }
}
