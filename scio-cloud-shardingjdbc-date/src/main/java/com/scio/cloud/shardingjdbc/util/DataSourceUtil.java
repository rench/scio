package com.scio.cloud.shardingjdbc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.apache.commons.collections4.map.LazyMap;
import org.springframework.jdbc.datasource.AbstractDataSource;

import com.zaxxer.hikari.HikariDataSource;

public class DataSourceUtil {
    private static final String HOST = "localhost";

    private static final int PORT = 3306;

    private static final String USER_NAME = "root";

    private static final String PASSWORD = "root";

    public static DataSource createDataSource(final String dataSourceName) {

        return new LazyDataSource(() -> {
            HikariDataSource result = new HikariDataSource();
            result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
            result.setJdbcUrl(String.format(
                "jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8", HOST,
                PORT, dataSourceName));
            result.setUsername(USER_NAME);
            result.setPassword(PASSWORD);
            return result;
        });
    }

    public static class LazyDataSource extends AbstractDataSource {

        private DataSource target;

        private Supplier<DataSource> supplier;

        public LazyDataSource(Supplier<DataSource> supplier) {
            this.supplier = supplier;
        }

        @Override
        public Connection getConnection() throws SQLException {
            synchronized (this) {
                if (target == null) {
                    target = supplier.get();
                }
                return target.getConnection();
            }

        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            synchronized (this) {
                if (target == null) {
                    target = supplier.get();
                }
                return target.getConnection(username, password);
            }
        }
    }

}
