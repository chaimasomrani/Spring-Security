package com.example.demo;



import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
@RestController
public class AccountRestController {
	@Autowired
	AccountService accountService;
	
	@GetMapping(path = "/users")
	@PostAuthorize("hasAuthority('USER')")
	public List<AppUser> appUsers()
	{return accountService.listUsers();}
	
	@PostMapping(path = "/users")
	@PostAuthorize("hasAuthority('ADMIN')")
	public AppUser saveUser(@RequestBody AppUser appUser) {
		return accountService.addNewUser(appUser);}
	
	@PostMapping(path = "/roles")
	@PostAuthorize("hasAuthority('ADMIN')")
	public AppRole saveRole(@RequestBody AppRole appRole) {
		return accountService.addNewRole(appRole);}
	
	@PostMapping(path = "/addRolelToUser")
	public Void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
		return accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());}
	
	@GetMapping(path="/refreshToken")
	public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String authToken=request.getHeader("Authorization");
		if(authToken!=null && authToken.startsWith("Bearer ")) {
	try {
				
				String jwt=authToken.substring(7);
				Algorithm algorithm=Algorithm.HMAC256("mySecret1234");
				JWTVerifier jwtVerifier=JWT.require(algorithm).build();
				DecodedJWT decodedJWT=jwtVerifier.verify(jwt);
				String username=decodedJWT.getSubject();
				AppUser appUser=accountService.loadUserByUsername(username);
				String jwtAccessToken=JWT.create()
						.withSubject(appUser.getUsername())
						.withExpiresAt(new Date (System.currentTimeMillis()+1*60*1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("role", appUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
						
						.sign(algorithm);
				Map<String,String> idToken=new HashMap<>();
				//Map<String,String> idToken=new HashMap<>();
				idToken.put("access-token", jwtAccessToken);
				idToken.put("refresh-token", jwt);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
				
				
			} catch (Exception e) {
				// TODO: handle exception
				throw e;
			}
			
		}
		else {
			throw new RuntimeException("Refresh token required");
		}
		
	}
	
		
		
	
	}

@Data
class RoleUserForm{
	private String username;
	private String roleName;
}
	


