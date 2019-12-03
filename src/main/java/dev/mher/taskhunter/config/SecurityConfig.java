package dev.mher.taskhunter.config;

import dev.mher.taskhunter.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 1:29 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final CryptoService cryptoService;

    @Autowired
    public SecurityConfig(DataSource dataSource, CryptoService cryptoService) {
        this.dataSource = dataSource;
        this.cryptoService = cryptoService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/**").permitAll();

//        http
//                .authorizeRequests()
//                .antMatchers("/", "v1/authentication/sign-up", "v1/authentication/confirm/*").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("v1/authentication/sign-in")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**");
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
//       final String queryString = "SELECT user_id AS \"userId\",\n" +
//                "       email AS \"email\",\n" +
//                "       password AS \"password\",\n" +
//                "       created_at AS \"createdAt\",\n" +
//                "       first_name AS \"firstName\",\n" +
//                "       last_name AS \"lastName\",\n" +
//                "       is_active AS \"isActive\"\n" +
//                "FROM users\n" +
//                "WHERE email=? AND is_active=?\n" +
//                "LIMIT 1;";
//        managerBuilder
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery(queryString)
//                .passwordEncoder(cryptoService.getEncoder());
//    }


}