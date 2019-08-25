package com.petpool.application.util.ip;

import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.util.List;

public class IpParserImpl implements IpParser {
    @Override
    public @NotNull String parse(HttpHeaders headers) {
        if (headers == null) {
            return "";
        }
        InetSocketAddress address = headers.getHost();
        if (address != null) {
            String ip = address.getHostName();
            if (ip != null && !(ip.equals(""))) {
                return ip;
            }
        }
        List<String> list = headers.get("HTTP_X_FORWARDED_FOR");
        if (list == null) {
            return "";
        } else {
            return list.get(0);
        }
    }
}
