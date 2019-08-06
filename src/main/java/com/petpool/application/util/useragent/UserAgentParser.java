package com.petpool.application.util.useragent;

/**
 * User-agent string parser
 */
public interface UserAgentParser {

  /**
   * Parse user-agent header's field string
   *
   * @param userAgent string represents user-agent header's field
   * @return result of parsing
   */
  UserAgentParserResult parse(String userAgent);

}
