package com.petpool.application.util;

import lombok.Data;

/**
 * The data class to wrap and store the data base properties.
 */
@Data
public class DataBaseProperties {

  private String url;
  private String name;
  private String password;
  private String driverClass;
  private int hibernatePoolSize;
  private String hibernateSessionContextClass;
  private boolean hibernateShowSql;
  private String hibernateDialect;
}
