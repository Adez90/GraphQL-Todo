package com.adez.graphql.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EntityScan(basePackages = {"com.adez.graphql.model.entity"})
@EnableJpaRepositories(basePackages = {"com.adez.graphql.repository"})
@EnableTransactionManagement
public class Config extends WebSecurityConfigurerAdapter{
	@Bean
    public Auth0Client auth0Client() {
        return new Auth0Client("domain", "client_id", "client_secret");
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtWebSecurityConfigurer
                .forHS256("domain", "client_id", "client_secret".getBytes())
                .configure(http)
                .authorizeRequests()
                	.antMatchers("/graphql").permitAll()
                	.antMatchers("/getSchema").permitAll();
    }
}
