package com.petpool.application.util;

import javax.annotation.PostConstruct;
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
  private String hibernateHbm2ddlAuto;
  private int poolMinIdle;
  private int poolMaxIdle;
  private int poolMaxTotal;
  private LocalDataBaseProperties localDataBaseProperties;

  public DataBaseProperties(LocalDataBaseProperties localDataBaseProperties) {
    this.localDataBaseProperties = localDataBaseProperties;
  }

  @PostConstruct
  private void fillWithLocalProperties() {
    url = localDataBaseProperties.getUrl();
    name = localDataBaseProperties.getName();
    password = localDataBaseProperties.getPassword();
    hibernateHbm2ddlAuto = localDataBaseProperties.getHibernateHbm2ddlAuto();
  }

}

