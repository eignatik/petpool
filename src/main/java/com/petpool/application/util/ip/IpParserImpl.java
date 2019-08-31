package com.petpool.application.util.ip;

import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.util.List;

public class IpParserImpl implements IpParser {
    private static String DEFAULT_PARSE_VALUE = "";

    @Override
    public @NotNull String parse(HttpHeaders headers) {
        if (headers == null) {
            return DEFAULT_PARSE_VALUE;
        }

        InetSocketAddress address = headers.getHost();

        if (address != null) {
            String ip = address.getHostName();
            if (!(ip.isEmpty())) {
                return ip;
            }
        }
        List<String> list = headers.get("HTTP_X_FORWARDED_FOR");
        if (list == null) {
            return DEFAULT_PARSE_VALUE;
        } else {
            String[] listIp = list.get(0).split(",");
            return listIp[0];
        }
    }
}
