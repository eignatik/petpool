package com.petpool.config;


import com.petpool.constants.HibernateProperties;
import com.petpool.db.DataBaseProperties;
import com.petpool.util.EncryptionTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@PropertySource("environment.properties")
public class ApplicationConfig {

    private static final String PACKAGE_TO_SCAN = "com.petpool.db";
    private static final String ENC_ALGORITHM = "AES/CBC/PKCS5Padding";

    @Value("${encryption.key}")
    private String encKey;

    @Bean
    public EncryptionTool encryptionTool() {
        return new EncryptionTool(ENC_ALGORITHM, encKey);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource restDataSource, Properties hibernateProperties) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(restDataSource);
        sessionFactory.setPackagesToScan(PACKAGE_TO_SCAN);
        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    @Bean
    public DataSource restDataSource(EncryptionTool encryptionTool, DataBaseProperties dataBaseProperties) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dataBaseProperties.getDriverClass());
        dataSource.setUrl(dataBaseProperties.getUrl());
        dataSource.setUsername(dataBaseProperties.getName());
        dataSource.setPassword(encryptionTool.decrypt(dataBaseProperties.getPassword()));
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
        props.setProperty(HibernateProperties.DRIVER_CLASS.getProperty(), dataBaseProperties.getDriverClass());
        props.setProperty(
                HibernateProperties.POOL_SIZE.getProperty(),
                String.valueOf(dataBaseProperties.getHibernatePoolSize()));
        props.setProperty(HibernateProperties.URL.getProperty(), dataBaseProperties.getUrl());
        props.setProperty(
                HibernateProperties.CURRENT_SESSION_CONTEXT_CLASS.getProperty(),
                dataBaseProperties.getHibernateSessionContextClass());
        props.setProperty(
                HibernateProperties.SHOW_SQL.getProperty(),
                String.valueOf(dataBaseProperties.isHibernateShowSql()));
        props.setProperty(HibernateProperties.DIALECT.getProperty(), dataBaseProperties.getHibernateDialect());
        return props;
    }

    @Bean
    @ConfigurationProperties(prefix = "db")
    public DataBaseProperties dataBaseProperties() {
        return new DataBaseProperties();
    }

}
