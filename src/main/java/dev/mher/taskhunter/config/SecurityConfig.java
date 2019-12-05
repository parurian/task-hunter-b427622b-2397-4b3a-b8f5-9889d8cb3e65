package dev.mher.taskhunter.config;

import dev.mher.taskhunter.controllers.filters.AuthenticationFilter;
import dev.mher.taskhunter.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

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
    private final AuthenticationFilter authenticationFilter;
    private final CryptoService cryptoService;

    @Value("${app.frontend.baseUrl}")
    private String frontendBaseUrl;

    @Value("${cors.allowed.methods}")
    private String corsAllowedMethods;

    @Value("${cors.allowed.headers}")
    private String corsAllowedHeaders;


    @Autowired
    public SecurityConfig(DataSource dataSource, AuthenticationFilter authenticationFilter, CryptoService cryptoService) {
        this.dataSource = dataSource;
        this.authenticationFilter = authenticationFilter;
        this.cryptoService = cryptoService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                    .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers(HttpMethod.POST, "/v1/authentication/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(frontendBaseUrl));

        configuration.setAllowedMethods(Arrays.asList(corsAllowedMethods.split(",")));

        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList(corsAllowedHeaders.split(",")));

        final UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);

        return configurationSource;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**");
    }

}