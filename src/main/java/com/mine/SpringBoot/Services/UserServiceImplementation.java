package com.mine.SpringBoot.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mine.SpringBoot.config.JwtProvider;
import com.mine.SpringBoot.model.User;
import com.mine.SpringBoot.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService{
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getUserProfile(String jwt) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		return userRepository.findByEmail(email);
	}
	
	@Override
	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}
}
