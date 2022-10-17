package com.example.demo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppRoleRepository appRoleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public AppUser addNewUser(AppUser appUser) {
		// TODO Auto-generated method stub
		String pw=appUser.getPassword();
		appUser.setPassword(passwordEncoder.encode(pw));
		return appUserRepository.save(appUser);
	}

	@Override
	public AppRole addNewRole(AppRole appRole) {
		// TODO app-generated method stub
		return appRoleRepository.save(appRole);
	}

	@Override
	public Void addRoleToUser(String username, String roleName) {
		// TODO Auto-generated method stub
		AppUser appUser=appUserRepository.findByUsername(username);
		AppRole appRole=appRoleRepository.findByRoleName(roleName);
		
		appUser.getAppRoles().add(appRole);
		return null;
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		// TODO Auto-generated method stub
		return appUserRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> listUsers() {
		// TODO Auto-generated method stub
		return appUserRepository.findAll();
	}

}
