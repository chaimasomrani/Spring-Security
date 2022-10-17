package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
//import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AccountService accountService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				AppUser appUser = accountService.loadUserByUsername(username);
				Collection<GrantedAuthority> authorities = new ArrayList<>();
				appUser.getAppRoles().forEach(r -> {
					authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
				});
				return new org.springframework.security.core.userdetails.User(appUser.getUsername(),
						appUser.getPassword(), authorities);

			}
		});
		// BCryptPasswordEncoder bcpe=new BCryptPasswordEncoder();
		// auth.inMemoryAuthentication().withUser("admin").password(bcpe.encode("123")).roles("ADMIN","USER");
		// auth.inMemoryAuthentication().withUser("user").password(bcpe.encode("123")).roles("USER");
		// auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/h2-console/**","/refreshToken/**","/login/**").permitAll();
		// http.formLogin();
		
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	// http.authorizeRequests().antMatchers("/admin/*").hasRole("ADMIN");
	// http.authorizeRequests().antMatchers("/user/*").hasRole("USER");
	// http.exceptionHandling().accessDeniedPage("/403");}
	// @Bean
	// BCryptPasswordEncoder getBCPE() {
	// return new BCryptPasswordEncoder();
}
