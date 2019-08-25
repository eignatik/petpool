package com.petpool.application.util.ip;

import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.net.InetSocketAddress;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IpParserTest {
    private IpParserImpl impl = new IpParserImpl();

    @Test
    public void parserHeaderThenLocalHost() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("localhost", 5050);
        headers.setHost(address);
        assertThat(impl.parse(headers), is("localhost"));
    }

    @Test
    public void parserHeaderThenHaveIp() {
        HttpHeaders headers = new HttpHeaders();
        InetSocketAddress address = new InetSocketAddress("10.0.0.1", 8080);
        headers.setHost(address);
        assertThat(impl.parse(headers), is("10.0.0.1"));
    }

    @Test
    public void parserHeaderThenEmptyHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String parse = impl.parse(headers);
        assertThat(parse, is(""));
    }
}
