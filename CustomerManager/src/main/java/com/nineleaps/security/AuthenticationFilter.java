package com.nineleaps.security;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.Customer;
import com.nineleaps.model.CustomerDTO;
import com.nineleaps.model.LoginRequestModel;
import com.nineleaps.service.CustomerService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private CustomerService customerService;
	private Environment environment;
	private AuthenticationManager authenticationManager;

	public AuthenticationFilter(CustomerService customerService, Environment environment,
			AuthenticationManager authenticationManager) {
		this.customerService = customerService;
		this.environment = environment;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {

			LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getName(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String name = ((User) auth.getPrincipal()).getUsername();
		CustomerDTO customerDetails = customerService.getUserDetailsName(name);

		String token = Jwts.builder().setSubject(customerDetails.getName())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret")).compact();

		res.addHeader("token", token);
		res.addHeader("name", customerDetails.getName());
	}
}
