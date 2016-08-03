package org.forweb.database;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;


@Configuration
public class HibernateSupport
{
    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String SORT = "sort";

    public static String packagesToScan;
    public static boolean AUTOCOMMIT = true;

    public static Properties hibernateProperties = new Properties();

    public static void init(String host, String user, String password, String database, String packagesToScan) {
        init(host, user, password, database, packagesToScan, "com.mysql.jdbc.Driver");
    }

    public static void init(String host, String user, String password, String database, String packagesToScan, String driverClassName) {
        init(host, user, password, database, packagesToScan, driverClassName, "org.forweb.database.MySqlDialect");
    }

    public static void init(String host, String user, String password, String database, String packagesToScan, String driverClassName, String dialectClassName) {
        HibernateSupport.url = "jdbc:mysql://" + host + "/" + database;
        HibernateSupport.username = user;
        HibernateSupport.password = password;
        HibernateSupport.driverClassName = driverClassName;
        HibernateSupport.packagesToScan = packagesToScan;
        HibernateSupport.dialectClassName = dialectClassName;
    }

    @Bean(name="sortHandlerMethodArgumentResolver")
    public SortHandlerMethodArgumentResolver sortHandlerMethodArgumentResolver() {
        SortHandlerMethodArgumentResolver sortable = new SortHandlerMethodArgumentResolver();
        sortable.setSortParameter(SORT);
        return sortable;
    }
    @Bean(name="pageableHandlerMethodArgumentResolver")
    static PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver(SortHandlerMethodArgumentResolver sortHandlerMethodArgumentResolver) {
        PageableHandlerMethodArgumentResolver pageable = new PageableHandlerMethodArgumentResolver(sortHandlerMethodArgumentResolver);
        pageable.setMaxPageSize(100);
        pageable.setOneIndexedParameters(true);
        pageable.setPageParameterName(PAGE);
        pageable.setSizeParameterName(SIZE);
        pageable.setQualifierDelimiter(null);
        pageable.setPrefix(null);
        return pageable;
    }

    public static void setDebug(Boolean debug) {
        hibernateGenerateStatistics = debug;
        hibernateShowSql = debug;
        hibernateFormatSql = debug;
        hibernateUseSqlComments = debug;
    }

    private static String driverClassName;
    public static String url;
    private static String username;
    private static String password;

    public static String hibernateConnectionCharset = "UTF-8";
    public static String dialectClassName = "org.forweb.database.MySqlDialect";
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
        hibernateProperties.put("hibernate.connection.url", url);
        hibernateProperties.put("hibernate.connection.username", username);
        hibernateProperties.put("hibernate.connection.password", password);
        hibernateProperties.put("hibernate.connection.driver_class", driverClassName);
        hibernateProperties.put("hibernate.connection.pool_size", 20);

        hibernateProperties.put("hsqldb.write_delay_millis", 0);
        hibernateProperties.put("hibernate.c3p0.validate", true);
        hibernateProperties.put("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
        hibernateProperties.put("hibernate.c3p0.min_size", 5);
        hibernateProperties.put("hibernate.c3p0.max_size", 20);
        hibernateProperties.put("hibernate.c3p0.max_statements", 50);
        hibernateProperties.put("hibernate.c3p0.preferredTestQuery", "SELECT 1;");
        hibernateProperties.put("hibernate.c3p0.testConnectionOnCheckout", true);
        hibernateProperties.put("hibernate.c3p0.idle_test_period", 3000);
        hibernateProperties.put("hibernate.c3p0.acquireRetryAttempts", 5);
        hibernateProperties.put("hibernate.c3p0.acquireRetryDelay", 200);
        hibernateProperties.put("hibernate.c3p0.timeout", 300);

        hibernateProperties.put("hibernate.connection.charSet", hibernateConnectionCharset);
        hibernateProperties.put("hibernate.connection.autocommit", AUTOCOMMIT);

        hibernateProperties.put("hibernate.dialect", dialectClassName);
        hibernateProperties.put("hibernate.ejb.naming_strategy", hibernateEjbNamingStrategy);
        hibernateProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        hibernateProperties.put("hibernate.generate_statistics", hibernateGenerateStatistics);

        hibernateProperties.put("hibernate.show_sql", hibernateShowSql);
        hibernateProperties.put("format_sql", hibernateFormatSql);
        hibernateProperties.put("hibernate.use_sql_comments", hibernateUseSqlComments);
        hibernateProperties.put("use_sql_comments", hibernateUseSqlComments);

        hibernateProperties.put("hibernate.jdbc.batch_size", hibernateJdbcBatchSize);
        hibernateProperties.put("hibernate.order_inserts", true);
        hibernateProperties.put("hibernate.order_updates", true);
        hibernateProperties.put("hibernate.cache.region_prefix", hibernateCacheRegionPrefix);
        return hibernateProperties;
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