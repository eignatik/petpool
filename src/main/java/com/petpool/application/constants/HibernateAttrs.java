package com.petpool.application.constants;

public enum HibernateAttrs {

  DRIVER_CLASS("hibernate.connection.driver_class"),
  POOL_SIZE("hibernate.connection.pool_size"),
  URL("hibernate.connection.url"),
  CURRENT_SESSION_CONTEXT_CLASS("hibernate.current_session_context_class"),
  SHOW_SQL("hibernate.show_sql"),
  DIALECT("hibernate.dialect");

  private String property;

  HibernateAttrs(String property) {
    this.property = property;
  }

  public String getProperty() {
    return property;
  }
}
