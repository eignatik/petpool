package com.petpool.application.util.ip;

import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;

public class IpParserImpl implements IpParser {
    @Override
    public @NotNull String parse(HttpHeaders headers) {
        String ip = headers.getHost().getHostName();
        if (ip.equals("localhost")) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}
