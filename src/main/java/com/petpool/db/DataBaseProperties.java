package com.petpool.db;

public class DataBaseProperties {
    private String url;
    private String name;
    private String password;
    private String driverClass;
    private int hibernatePoolSize;
    private String hibernateSessionContextClass;
    private boolean hibernateShowSql;
    private String hibernateDialect;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public int getHibernatePoolSize() {
        return hibernatePoolSize;
    }

    public void setHibernatePoolSize(int hibernatePoolSize) {
        this.hibernatePoolSize = hibernatePoolSize;
    }

    public String getHibernateSessionContextClass() {
        return hibernateSessionContextClass;
    }

    public void setHibernateSessionContextClass(String hibernateSessionContextClass) {
        this.hibernateSessionContextClass = hibernateSessionContextClass;
    }

    public boolean isHibernateShowSql() {
        return hibernateShowSql;
    }

    public void setHibernateShowSql(boolean hibernateShowSql) {
        this.hibernateShowSql = hibernateShowSql;
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public void setHibernateDialect(String hibernateDialect) {
        this.hibernateDialect = hibernateDialect;
    }
}
