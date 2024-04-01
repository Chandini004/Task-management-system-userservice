package com.mine.SpringBoot.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mine.SpringBoot.model.User;
import com.mine.SpringBoot.repository.UserRepository;

@Service
public class CustomUserServiceImplementation implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("user not found by email"+username);
		}
		List<GrantedAuthority>authorities= new ArrayList<>();
		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
	}
}
