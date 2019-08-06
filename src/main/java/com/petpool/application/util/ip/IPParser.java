package com.petpool.application.util.ip;

import javax.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;

public interface IPParser {
  @NotNull String parse(HttpHeaders headers);
}
