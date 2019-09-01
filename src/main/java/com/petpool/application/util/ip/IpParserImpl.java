package com.petpool.application.util.ip;

import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.util.List;

public class IpParserImpl implements IpParser {
    private final static String DEFAULT_PARSE_VALUE = "";

    @Override
    public @NotNull String parse(HttpHeaders headers) {
        if (headers == null) {
            return DEFAULT_PARSE_VALUE;
        }

        InetSocketAddress address = headers.getHost();

        if (address != null) {
            String ip = address.getHostName();
            System.out.println("ip" + ip);
            if (!(ip.isEmpty())) {
                return ip;
            }
        }
        List<String> list = headers.get("HTTP_X_FORWARDED_FOR");
        if (list == null || list.isEmpty()) {
            return DEFAULT_PARSE_VALUE;
        }

        if (list.get(0) != null) {
            String[] listIp = list.get(0).split(",");
            return listIp[0];
        }

        return DEFAULT_PARSE_VALUE;
    }
}
