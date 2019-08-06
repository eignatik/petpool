package com.petpool.application.util.useragent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAgentParserResult {

  private OS os;
  private Browser browser;
}
