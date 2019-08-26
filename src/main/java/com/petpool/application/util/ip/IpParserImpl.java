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
            if (ip != null && !(ip.isEmpty())) {
                return ip;
            }
        }
        List<String> list = headers.get("HTTP_X_FORWARDED_FOR");
        if (list == null) {
            return DEFAULT_PARSE_VALUE;
        } else {
            // TODO: надо будет потом на реальных пользователях проверить
            return list.get(0);
        }
    }
}
