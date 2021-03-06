package com.service.core.dbConfig;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mapstruct.MapperConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("com.service.core.dao")
public class TestDBConfig {

//    @Value("${db.driver}")
//    private String DB_DRIVER;
//    @Value("${db.url}")
//    private String DB_URL;
//    @Value("${db.username}")
//    private String DB_USERNAME;
//    @Value("${db.password}")
//    private String DB_PASSWORD;

    @Value("${c3p0.config.path}")
    private String C3P0_CONFIG_PATH;
    @Value("${hibernate.dialect}")
    private String HIBERNATE_DIALECT;
    @Value("${hibernate.show.sql}")
    private String HIBERNATE_SHOW_SQL;
    @Value("${hibernate.hbm2ddl.auto}")
    private String HIBERNATE_HBM2DDL_AUTO;
    @Value("${entity.manager.packages.to.scan}")
    private String PACKAGES_TO_SCAN;

//    @Bean
//    @Primary
//    public DataSource dataSource(){
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(DB_DRIVER);
//        dataSource.setUrl(DB_URL);
//        dataSource.setUrl(DB_USERNAME);
//        dataSource.setUrl(DB_PASSWORD);
//        return dataSource;
//    }

    @Bean
    @Primary
    public DataSource dataSource() {
        System.setProperty("com.mchange.v2.c3p0.cfg.xml", C3P0_CONFIG_PATH);
        return new ComboPooledDataSource();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

//    @Bean
//    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
//        return transactionManager;
//    }

    @Bean
    public JpaTransactionManager transactionManager(
            @Qualifier("entityManagerFactory")
                    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
        return manager;
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {

        LocalContainerEntityManagerFactoryBean
                entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN);

        Properties properties = new Properties();
        properties.put("hibernate.dialect", HIBERNATE_DIALECT);
        properties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
        properties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
        entityManagerFactoryBean.setJpaProperties(properties);

        return entityManagerFactoryBean;
    }


}
