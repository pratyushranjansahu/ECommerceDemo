package com.nineleaps.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nineleaps.service.CustomerService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	private Environment environment;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// for JWT Token
		http.csrf().disable();

		http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"))
		.and()
		.addFilter(getAuthenticationFilter());

	}
	
	private AuthenticationFilter getAuthenticationFilter() throws Exception
	{
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(customerService, environment, authenticationManager());
		//authenticationFilter.setAuthenticationManager(authenticationManager()); 
		//authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerService).passwordEncoder(bCryptPasswordEncoder);
    }
}
