package com.petpool.config;


import com.petpool.application.constants.HibernateAttrs;
import com.petpool.application.util.DataBaseProperties;
import com.petpool.application.util.EncryptionTool;
import com.petpool.application.util.LocalDataBaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@PropertySource({"environment.properties", "environment-local.properties"})
@EnableTransactionManagement
public class ApplicationConfig {

  private static final String DOMAIN_PACKAGE_TO_SCAN = "com.petpool.domain";
  private static final String ENC_ALGORITHM = "AES/CBC/PKCS5Padding";

  @Value("${encryption.key}")
  private String encKey;

  @Value("${useLocalDbProperties}")
  private boolean useLocalDbProperties;

  @Bean
  public EncryptionTool encryptionTool() {
    return new EncryptionTool(ENC_ALGORITHM, encKey);
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory(
      DataSource restDataSource,
      Properties hibernateProperties) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(restDataSource);
    sessionFactory.setPackagesToScan(DOMAIN_PACKAGE_TO_SCAN);
    sessionFactory.setHibernateProperties(hibernateProperties);
    return sessionFactory;
  }

  @Bean
  public DataSource restDataSource(
      EncryptionTool encryptionTool,
      DataBaseProperties dataBaseProperties) {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(dataBaseProperties.getDriverClass());
    dataSource.setUrl(dataBaseProperties.getUrl());
    dataSource.setUsername(dataBaseProperties.getName());
    dataSource.setPassword(useLocalDbProperties ?
        dataBaseProperties.getPassword() :
        encryptionTool.decrypt(dataBaseProperties.getPassword()));
    dataSource.setMinIdle(dataBaseProperties.getPoolMinIdle());
    dataSource.setMaxIdle(dataBaseProperties.getPoolMaxIdle());
    dataSource.setMaxTotal(dataBaseProperties.getPoolMaxTotal());
    return dataSource;
  }

  @Bean
  @Autowired
  public HibernateTransactionManager transactionManager(SessionFactory factory) {
    return new HibernateTransactionManager(factory);
  }

  @Bean
  public Properties hibernateProperties(DataBaseProperties dataBaseProperties) {
    Properties props = new Properties();

    props.setProperty(HibernateAttrs.HIBERNATE_HBM_2DDL_AUTO.getProperty(),
        dataBaseProperties.getHibernateHbm2ddlAuto());
    props.setProperty(
        HibernateAttrs.DRIVER_CLASS.getProperty(),
        dataBaseProperties.getDriverClass());
    props.setProperty(
        HibernateAttrs.POOL_SIZE.getProperty(),
        String.valueOf(dataBaseProperties.getHibernatePoolSize()));
    props.setProperty(
        HibernateAttrs.CURRENT_SESSION_CONTEXT_CLASS.getProperty(),
        dataBaseProperties.getHibernateSessionContextClass());
    props.setProperty(
        HibernateAttrs.SHOW_SQL.getProperty(),
        String.valueOf(dataBaseProperties.isHibernateShowSql()));
    props.setProperty(HibernateAttrs.DIALECT.getProperty(),
        dataBaseProperties.getHibernateDialect());
    return props;
  }

  @Bean
  @DependsOn("localDataBaseProperties")
  @ConfigurationProperties(prefix = "db")
  public DataBaseProperties dataBaseProperties(LocalDataBaseProperties localDataBaseProperties) {
    return new DataBaseProperties(localDataBaseProperties);
  }

  @Bean
  @ConfigurationProperties(prefix = "local.db")
  LocalDataBaseProperties localDataBaseProperties() {
    return new LocalDataBaseProperties();
  }

}
