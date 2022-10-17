package com.example.demo;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager=authenticationManager;
		// TODO Auto-generated constructor stub
	}

@Override
public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws AuthenticationException {
	System.out.println("attempt auth");
	// TODO Auto-generated method stub
String username=request.getParameter("username");
String password=request.getParameter("password");
System.out.println(username);
System.out.println(password);
UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(username, password);


	
	return authenticationManager.authenticate(authenticationToken); }

@Override
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		org.springframework.security.core.Authentication authResult) throws IOException, ServletException {
	// TODO Auto-generated method stub
	System.out.println("success auth");
	User user=(User) authResult.getPrincipal();
	Algorithm algo1=Algorithm.HMAC256("mySecret1234");
	String jwtAccessToken=JWT.create()
			.withSubject(user.getUsername())
			.withExpiresAt(new Date (System.currentTimeMillis()+1*60*1000))
			.withIssuer(request.getRequestURL().toString())
			.withClaim("role", user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
			
			.sign(algo1);
	
	String jwtRefreshToken=JWT.create()
			.withSubject(user.getUsername())
			.withExpiresAt(new Date (System.currentTimeMillis()+15*60*1000))
		.withIssuer(request.getRequestURL().toString())
		.sign(algo1);
			//.withClaim("role", user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
			
	Map<String,String> idToken=new HashMap<>();
	//Map<String,String> idToken=new HashMap<>();
	idToken.put("access-token", jwtAccessToken);
	idToken.put("refresh-token", jwtRefreshToken);
	response.setContentType("application/json");
	new ObjectMapper().writeValue(response.getOutputStream(), idToken);
	
	//new ObjectMapper().writeValue(response.getOutputStream(), idToken);
	//response.setHeader("Authorization", jwtAccessToken);
	
	
	

	
	
}



}
