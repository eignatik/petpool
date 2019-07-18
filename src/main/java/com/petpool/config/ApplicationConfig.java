package com.petpool.config;


import com.petpool.application.constants.HibernateAttrs;
import com.petpool.application.util.DataBaseProperties;
import com.petpool.application.util.EncryptionTool;
import com.petpool.application.util.LocalDataBaseProperties;
import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@PropertySource({"environment.properties", "environment-local.properties"})
@EnableJpaRepositories(basePackages = {"com.petpool.domain.model"})
@EnableTransactionManagement
public class ApplicationConfig {

  private static final String DOMAIN_PACKAGE_TO_SCAN = "com.petpool.domain";
  private static final String ENC_ALGORITHM = "AES/CBC/PKCS5Padding";

  @Value("${encryption.key}")
  private String encKey;


  @Bean
  public EncryptionTool encryptionTool() {
    return new EncryptionTool(ENC_ALGORITHM, encKey);
  }

  @Bean
  public EntityManagerFactory entityManagerFactory(
      LocalContainerEntityManagerFactoryBean factoryBean) {
    return factoryBean.getNativeEntityManagerFactory();
  }

  @Bean
  @Primary
  LocalContainerEntityManagerFactoryBean factoryBean(
      DataSource restDataSource,
      Properties hibernateProperties,
      JpaVendorAdapter jpaVendorAdapter,
      HibernateJpaDialect hibernateJpaDialect) {
    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setPackagesToScan(DOMAIN_PACKAGE_TO_SCAN);
    factoryBean.setDataSource(restDataSource);
    factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
    factoryBean.setJpaProperties(hibernateProperties);
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    factoryBean.setJpaDialect(hibernateJpaDialect);
    return factoryBean;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  @Bean
  public DataSource restDataSource(
      EncryptionTool encryptionTool,
      DataBaseProperties dataBaseProperties) {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(dataBaseProperties.getDriverClass());
    dataSource.setUrl(dataBaseProperties.getUrl());
    dataSource.setUsername(dataBaseProperties.getName());
    dataSource.setPassword(dataBaseProperties.isUseLocalDbProperties() ?
        dataBaseProperties.getPassword() :
        encryptionTool.decrypt(dataBaseProperties.getPassword()));
    dataSource.setMinIdle(dataBaseProperties.getPoolMinIdle());
    dataSource.setMaxIdle(dataBaseProperties.getPoolMaxIdle());
    dataSource.setMaxTotal(dataBaseProperties.getPoolMaxTotal());
    return dataSource;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  public HibernateJpaDialect hibernateJpaDialect() {
    return new HibernateJpaDialect();
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
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
