package com.mine.SpringBoot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mine.SpringBoot.Services.CustomUserServiceImplementation;
import com.mine.SpringBoot.config.JwtProvider;
import com.mine.SpringBoot.model.User;
import com.mine.SpringBoot.repository.UserRepository;
import com.mine.SpringBoot.request.LoginRequest;
import com.mine.SpringBoot.response.AuthResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomUserServiceImplementation customUserDetails;
	
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception{
		String email = user.getEmail();
		String password = user.getPassword();
		String role = user.getRole();
		String fullName =user.getFullName();
		
		User isEmailExist = userRepository.findByEmail(email);
		
		if(isEmailExist!=null) {
			throw new Exception("email already existed");
		}
		
		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setFullName(fullName);
		createdUser.setRole(role);
		createdUser.setPassword(passwordEncoder.encode(password));
		
	    userRepository.save(createdUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = JwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Register success");
		authResponse.setStatus(true);
		
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
		
		
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest){
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		
		System.out.println(username+ "...." + password);
		
		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = JwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		
		authResponse.setMessage("Login success");
		authResponse.setJwt(token);
		authResponse.setStatus(true);
		
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
		
	}
	
	private Authentication authenticate(String username , String password) {
		UserDetails userDetails = customUserDetails.loadUserByUsername(username);
		
		System.out.println("sign in userDetails-null" + userDetails);
		
		if(userDetails==null) {
			System.out.println("sign in userDetails-null" + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			System.out.println("sign in userDetails - password not match " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}
}
