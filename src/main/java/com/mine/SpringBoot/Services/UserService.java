package com.mine.SpringBoot.Services;
import java.util.List;

import com.mine.SpringBoot.model.User;

public interface UserService {	
		
		public User getUserProfile(String jwt);
	
		public List<User> getAllUsers(); 
}
