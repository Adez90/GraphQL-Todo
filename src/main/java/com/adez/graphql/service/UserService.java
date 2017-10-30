package com.adez.graphql.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adez.graphql.model.dto.UserDto;
import com.adez.graphql.model.entity.UserEntity;
import com.adez.graphql.repository.UserRepository;

@Service
public class UserService {
	private static UserRepository userRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	UserEntity userEntity;
	UserDto userDto;
	
	ArrayList<UserEntity> userEntitys;
	ArrayList<UserDto> userDtos;
	
	@Autowired
	public void setRepository(UserRepository userRepository){
		UserService.userRepository = userRepository;
	}
	
	public UserDto save(UserDto userDto){
		userEntity = modelMapper.map(userDto, UserEntity.class);
		
		userEntity = userRepository.save(userEntity);
		
		userDto = modelMapper.map(userEntity, UserDto.class);
		
		return userDto;
	}
	
	public UserDto getById(Long id){
		if(userRepository != null){
			userEntity = userRepository.findOne(id);
			
			userDto = modelMapper.map(userEntity, UserDto.class);
			
			return userDto;
		}
		else
			return new UserDto();
	}
	
	public UserDto getByEmail(String email){
		if(userRepository.findByEmail(email) == null)
			return null;
		
		userEntity = userRepository.findByEmail(email);
		
		userDto = modelMapper.map(userEntity, UserDto.class);
		
		return userDto;
	}

	public UserDto getByEmailAndPassword(String email, String password) {
		userEntity = userRepository.findByEmailAndPassword(email, password);
		
		userDto = modelMapper.map(userEntity, UserDto.class);
		
		return userDto;
	}
	
	// Validates users input fields
	public Boolean userValidation(String email, String password, String firstname, String lastname){

		if(email != null && email.length() > 4 && password != null && password.length() > 1  && firstname != null
				&& firstname.length() > 1  && lastname != null && lastname.length() > 1)
			return true;
		return false;
	}
	
	//Check if user is in database
	public Boolean checkEmail(String email) {
		if(userRepository.findByEmail(email) == null)
			return false;
		else
			return true;
	}
}
