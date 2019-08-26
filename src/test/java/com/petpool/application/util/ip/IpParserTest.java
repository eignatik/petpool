package com.petpool.application.util.ip;

import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.net.InetSocketAddress;

import static org.junit.Assert.assertTrue;

public class IpParserTest {
    private IpParserImpl impl = new IpParserImpl();
    private static String DEFAULT_PARSE_VALUE = "";

    @Test
    public void parserHeaderThenLocalHost() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("localhost", 5050);
        headers.setHost(address);
        String ip = impl.parse(headers);
        assertTrue(ip.equals("localhost"));
    }

    @Test
    public void parserHeaderThenHaveIp() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("10.0.0.1", 8080);
        headers.setHost(address);
        String ip = impl.parse(headers);
        assertTrue(ip.equals("10.0.0.1"));
    }

    @Test
    public void parserHeaderThenEmptyHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String ip = impl.parse(headers);
        assertTrue(ip.equals(DEFAULT_PARSE_VALUE));
    }

    @Test
    public void parserHeaderThenNullHeaders() {
        HttpHeaders headers = null;
        String ip = impl.parse(headers);
        assertTrue(ip.equals(DEFAULT_PARSE_VALUE));
    }

    @Test
    public void parserHeaderThenByForwardedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String rightIp = "120.10.7.9";
        headers.set("HTTP_X_FORWARDED_FOR", rightIp);
        String ip = impl.parse(headers);
        assertTrue(ip.equals(rightIp));
    }
}
