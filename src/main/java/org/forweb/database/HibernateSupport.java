package org.forweb.database;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
public class HibernateSupport
{
    public static String packagesToScan;
    public static boolean AUTOCOMMIT = true;

    public static void init(String host, String user, String password, String database, String packagesToScan) {
        init(host, user, password, database, packagesToScan, "com.mysql.jdbc.Driver");
    }

    public static void init(String host, String user, String password, String database, String packagesToScan, String driverClassName) {
        HibernateSupport.url = "jdbc:mysql://" + host + "/" + database;
        HibernateSupport.username = user;
        HibernateSupport.password = password;
        HibernateSupport.driverClassName = driverClassName;
        HibernateSupport.packagesToScan = packagesToScan;
    }

    public static void setDebug(Boolean debug) {
        hibernateGenerateStatistics = debug;
        hibernateShowSql = debug;
        hibernateFormatSql = debug;
        hibernateUseSqlComments = debug;
    }

    private static String driverClassName;
    private static String url;
    private static String username;
    private static String password;

    public static String hibernateConnectionCharset = "UTF-8";
    public static String hibernateDialect = "org.forweb.database.MySqlDialect";
    public static String hibernateEjbNamingStrategy = "org.hibernate.cfg.ImprovedNamingStrategy";
    public static String hibernateHbm2ddlAuto = "validate";
    public static Boolean hibernateGenerateStatistics = false;
    public static Boolean hibernateShowSql = false;
    public static Boolean hibernateFormatSql = false;
    public static Boolean hibernateUseSqlComments = false;
    public static Integer hibernateJdbcBatchSize = 50;
    public static String hibernateCacheRegionPrefix = "";

    @Bean(name = "hibernateProperties")
    public Properties hibernateProperties() {
        Properties properties = new Properties();

        properties.put("hibernate.connection.url", url);
        properties.put("hibernate.connection.username", username);
        properties.put("hibernate.connection.password", password);
        properties.put("hibernate.connection.driver_class", driverClassName);
        properties.put("hibernate.connection.pool_size", 20);
        //properties.put("shutdown", true);
        properties.put("hsqldb.write_delay_millis", 0);
        properties.put("hibernate.c3p0.idle_test_period", 300);
        properties.put("hibernate.c3p0.timeout", 120);


        properties.put("hibernate.connection.charSet", hibernateConnectionCharset);
        properties.put("hibernate.connection.autocommit", AUTOCOMMIT);

        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.ejb.naming_strategy", hibernateEjbNamingStrategy);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        properties.put("hibernate.generate_statistics", hibernateGenerateStatistics);

        //properties.put("hibernate.cache.region.factory_class", org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory);
        //properties.put("#hibernate.cache.use_second_level_cache", "true");
        //properties.put("#hibernate.cache.use_query_cache", "true");

        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("format_sql", hibernateFormatSql);
        properties.put("hibernate.use_sql_comments", hibernateUseSqlComments);
        properties.put("use_sql_comments", hibernateUseSqlComments);

        properties.put("hibernate.jdbc.batch_size", hibernateJdbcBatchSize);
        properties.put("hibernate.order_inserts", true);
        properties.put("hibernate.order_updates", true);
        properties.put("hibernate.cache.region_prefix", hibernateCacheRegionPrefix);
        // properties.put("hibernate.current_session_context_class", "thread");
        return properties;
    }


    @Bean(name = "dataSource")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        // dataSource.setDefaultAutoCommit(AUTOCOMMIT);
        return dataSource;
    }

    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory(org.hibernate.cfg.Configuration configuration, ServiceRegistry serviceRegistry) {
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Bean
    org.hibernate.cfg.Configuration configuration(Properties hibernateProperties) {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.addPackage("entity");
        configuration.setProperties(hibernateProperties);
        return configuration;
    }

    @Bean(name = "serviceRegistry")
    public ServiceRegistry serviceRegistry(org.hibernate.cfg.Configuration configuration) {
        return new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
    }

    /////////////////////////////////////////////////////////////////////
    @Bean(name = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager out = new JpaTransactionManager();
        out.setEntityManagerFactory(entityManagerFactory);
        return out;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(Properties hibernateProperties, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("persistenceUnit");
        //em.setDataSource(dataSource);
        em.setJpaProperties(hibernateProperties);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPackagesToScan(packagesToScan);
        return em;
    }

    @Bean(name = "connectionProvider")
    ConnectionProvider connectionProvider(DataSource dataSource) {
        DatasourceConnectionProviderImpl out = new DatasourceConnectionProviderImpl();
        out.setDataSource(dataSource);
        return out;
    }
}