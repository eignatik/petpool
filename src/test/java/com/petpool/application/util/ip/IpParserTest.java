package com.petpool.application.util.ip;

import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;

import static org.testng.Assert.assertEquals;

public class IpParserTest {
    private IpParserImpl impl = new IpParserImpl();
    private final static String DEFAULT_PARSE_VALUE = "";

    @Test
    public void parse_forHeaderWhenLocalHost_returnsLocalHost() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("localhost", 5050);
        headers.setHost(address);
        String ip = impl.parse(headers);
        assertEquals(ip, "localhost");
    }

    @Test
    public void parse_forHeaderWhenHaveHostIp_returnsRightIp() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("10.0.0.1", 8080);
        headers.setHost(address);
        String ip = impl.parse(headers);
        assertEquals(ip, "10.0.0.1");
    }

    @Test
    public void parse_forHeaderWhenHaveEmptySocketAddress_returnsLocalHost() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("", 0);
        headers.setHost(address);
        String ip = impl.parse(headers);
        assertEquals(ip, "localhost");
    }

    @Test
    public void parse_forHeaderWhenSocketAddressNull_returnsLocalHost() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = null;
        headers.setHost(address);
        String ip = impl.parse(headers);
        assertEquals(ip, DEFAULT_PARSE_VALUE);
    }

    @Test
    public void parse_forHeaderWhenHeaderEmpty_returnsDefaultValue() {
        HttpHeaders headers = new HttpHeaders();
        String ip = impl.parse(headers);
        assertEquals(ip, DEFAULT_PARSE_VALUE);
    }

    @Test
    public void parse_forHeaderWhenHeaderNull_returnsDefaultValue() {
        HttpHeaders headers = null;
        String ip = impl.parse(headers);
        assertEquals(ip, DEFAULT_PARSE_VALUE);
    }

    @Test
    public void parse_forHeaderWhenHttpXForwardedFor_returnsRightIp() {
        HttpHeaders headers = new HttpHeaders();
        String rightIp = "120.10.7.9";
        headers.set("HTTP_X_FORWARDED_FOR", rightIp);
        String ip = impl.parse(headers);
        assertEquals(ip, rightIp);
    }

    @Test
    public void parse_forHeaderWhenHttpXForwardedForHaveSeparated_returnsFirstIp() {
        HttpHeaders headers = new HttpHeaders();
        String rightIp = "120.10.7.9";
        headers.set("HTTP_X_FORWARDED_FOR", "120.10.7.9, 10.0.0.1");
        String ip = impl.parse(headers);
        assertEquals(ip, rightIp);
    }

    @Test
    public void parse_forHeaderWhenHttpXForwardedForHaveElementNull_returnsDefaultValue() {
        // In this case, headers.get("HTTP_X_FORWARDED_FOR") the list will not be empty.
        HttpHeaders headers = new HttpHeaders();
        headers.set("HTTP_X_FORWARDED_FOR", null);
        String ip = impl.parse(headers);
        assertEquals(ip, DEFAULT_PARSE_VALUE);
    }
}
