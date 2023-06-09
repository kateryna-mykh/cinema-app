package cinema.dao.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTest {
    private static final String DATASOURCE_URL = "jdbc:hsqldb:mem:test";
    private static final String USERNAME = "DAOTestRunner";
    private static final String PASSWORD = "";
    private static final String DB_STRATEGY = "create-drop";
    protected String MOVIE_TITLE = "Mavka. The Forest Song";
    protected Long TEST_ID = 1L;
    protected String TEST_EMAIL = "test-mail@i.ua";
    protected String TEST_PASSWORD = "1234";
    protected String CINEMA_HALL_DESCR = "Multiplex";
    protected int CINEMA_HALL_CAPACITY = 200;
    
    protected interface DataSourceProvider {
        enum IdentifierStrategy {
            IDENTITY, SEQUENCE
        }

        enum Database {
            HSQLDB,
        }

        String hibernateDialect();

        DataSource dataSource();

        Class<? extends DataSource> dataSourceClassName();

        Properties dataSourceProperties();

        List<IdentifierStrategy> identifierStrategies();

        Database database();
    }

    private SessionFactory factory;

    @BeforeEach
    public void init() {
        factory = newSessionFactory();
    }

    private SessionFactory newSessionFactory() {
        Properties properties = getProperties();
        Configuration configuration = new Configuration().addProperties(properties);
        for (Class<?> entityClass : entities()) {
            configuration.addAnnotatedClass(entityClass);
        }
        String[] packages = packages();
        if (packages != null) {
            for (String scannedPackage : packages) {
                configuration.addPackage(scannedPackage);
            }
        }
        Interceptor interceptor = interceptor();
        if (interceptor != null) {
            configuration.setInterceptor(interceptor);
        }
        return configuration
                .buildSessionFactory(new StandardServiceRegistryBuilder()
                        .applySettings(properties).build());
    }

    protected Properties getProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", getDataSourceProvider().hibernateDialect());
        properties.put("hibernate.hbm2ddl.auto", DB_STRATEGY);

        properties.put("hibernate.connection.datasource", newDataSource());
        return properties;
    }

    protected DataSource newDataSource() {
        return getDataSourceProvider().dataSource();
    }

    protected abstract Class<?>[] entities();

    protected String[] packages() {
        return null;
    }

    protected Interceptor interceptor() {
        return null;
    }

    protected DataSourceProvider getDataSourceProvider() {
        return new HsqldbDataSourceProvider();
    }

    public SessionFactory getSessionFactory() {
        return factory;
    }

    public static class HsqldbDataSourceProvider implements DataSourceProvider {
        @Override
        public String hibernateDialect() {
            return "org.hibernate.dialect.HSQLDialect";
        }

        @Override
        public DataSource dataSource() {
            JDBCDataSource dataSource = new JDBCDataSource();
            dataSource.setUrl(DATASOURCE_URL);
            dataSource.setUser(USERNAME);
            dataSource.setPassword(PASSWORD);
            return dataSource;
        }

        @Override
        public Class<? extends DataSource> dataSourceClassName() {
            return JDBCDataSource.class;
        }

        @Override
        public Properties dataSourceProperties() {
            Properties properties = new Properties();
            properties.setProperty("url", DATASOURCE_URL);
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            return properties;
        }

        @Override
        public List<IdentifierStrategy> identifierStrategies() {
            return Arrays.asList(IdentifierStrategy.IDENTITY, IdentifierStrategy.SEQUENCE);
        }

        @Override
        public Database database() {
            return Database.HSQLDB;
        }
    }
}
