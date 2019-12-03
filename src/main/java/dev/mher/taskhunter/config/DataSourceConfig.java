package dev.mher.taskhunter.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 10:15 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.config.
 */

@Configuration
class DataSourceConfig {

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.dataSourceUrl}")
    private String dataSourceUrl;

    @Value("${jdbc.minimumIdle}")
    private int minimumIdle;

    @Value("${jdbc.maximumPoolSize}")
    private int maximumPoolSize;

    @Bean
    public DataSource getDataSource() {

        HikariConfig jdbcConfig = new HikariConfig();

        jdbcConfig.setJdbcUrl(dataSourceUrl);
        jdbcConfig.setUsername(username);
        jdbcConfig.setPassword(password);
        jdbcConfig.setMaximumPoolSize(maximumPoolSize);
        jdbcConfig.setMinimumIdle(minimumIdle);

        return new HikariDataSource(jdbcConfig);
    }
}