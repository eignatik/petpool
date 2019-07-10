package com.petpool.db;

import lombok.Data;

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
