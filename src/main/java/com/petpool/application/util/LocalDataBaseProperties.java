package com.petpool.application.util;

import lombok.Data;

@Data
public class LocalDataBaseProperties {

  private String url;
  private String name;
  private String password;
  private String user;
  private String hibernateHbm2ddlAuto;

}
