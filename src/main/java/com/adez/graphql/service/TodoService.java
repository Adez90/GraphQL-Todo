package com.adez.graphql.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adez.graphql.model.dto.TodoDto;
import com.adez.graphql.model.dto.UserDto;
import com.adez.graphql.model.entity.TodoEntity;
import com.adez.graphql.model.entity.UserEntity;
import com.adez.graphql.repository.TodoRepository;

@Service
public class TodoService {
	private static TodoRepository todoRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	TodoEntity todoEntity;
	TodoDto todoDto;
	
	ArrayList<TodoEntity> todoEntitys;
	ArrayList<TodoDto> todoDtos;
	
	@Autowired
	public void setRepository(TodoRepository todoRepository){
		TodoService.todoRepository = todoRepository;
	}
	
	public TodoDto save(TodoDto todoDto){
		todoEntity = modelMapper.map(todoDto, TodoEntity.class);
		
		todoEntity = todoRepository.save(todoEntity);
		
		todoDto = modelMapper.map(todoEntity, TodoDto.class);
		
		return todoDto;
	}

	public TodoDto getById(Long id) {
		todoEntity = todoRepository.findOne(id);
		
		todoDto = modelMapper.map(todoEntity, TodoDto.class);
		
		return todoDto;
	}
	
	public List<TodoDto> getByUser(UserDto userDto){
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		todoEntitys = (ArrayList<TodoEntity>) todoRepository.findByUser(userEntity);
				
		todoDtos = new ArrayList<TodoDto>();
		if(!todoEntitys.isEmpty()){
			for(int i = 0; i < todoEntitys.size(); i++){
				todoDto = modelMapper.map(todoEntitys.get(i), TodoDto.class);
				todoDtos.add(todoDto);
			}
			List<TodoDto> todoList = todoDtos;
			return todoList;
		}
		else{
			return null;
		}
	}
	
	public List<TodoDto> getByDate(UserDto userDto, String stringFromDate, String stringToDate){
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		Date fromDate = null;
		Date toDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			fromDate = formatter.parse(stringFromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			toDate = formatter.parse(stringToDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(toDate.before(fromDate)){
			Date temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}
		
		if(todoRepository.findByUserAndDates(userEntity.getId(), fromDate, toDate) == null){
			List<TodoDto> todoList = todoDtos;
			return todoList;
		}
		todoEntitys = (ArrayList<TodoEntity>) todoRepository.findByUserAndDates(userEntity.getId(), fromDate, toDate);
				
		todoDtos = new ArrayList<TodoDto>();
		if(!todoEntitys.isEmpty()){
			for(int i = 0; i < todoEntitys.size(); i++){
				todoDto = modelMapper.map(todoEntitys.get(i), TodoDto.class);
				todoDtos.add(todoDto);
			}
			List<TodoDto> todoList = todoDtos;
			return todoList;
		}
		else{
			return null;
		}
	}
	
	public List<TodoDto> getByDateAndIsFinished(UserDto userDto, String stringFromDate, String stringToDate){
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		Date fromDate = null;
		Date toDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			fromDate = formatter.parse(stringFromDate);
			toDate = formatter.parse(stringToDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(toDate.before(fromDate)){
			Date temp = fromDate;
			fromDate = toDate;
			toDate = temp;
		}
		
		if(todoRepository.findByUserAndDatesAndIsFinished(userEntity.getId(), fromDate, toDate) == null){
			List<TodoDto> todoList = todoDtos;
			return todoList;
		}
		
		todoEntitys = (ArrayList<TodoEntity>) todoRepository.findByUserAndDatesAndIsFinished(userEntity.getId(), fromDate, toDate);
				
		todoDtos = new ArrayList<TodoDto>();
		if(!todoEntitys.isEmpty()){
			for(int i = 0; i < todoEntitys.size(); i++){
				todoDto = modelMapper.map(todoEntitys.get(i), TodoDto.class);
				todoDtos.add(todoDto);
			}
			List<TodoDto> todoList = todoDtos;
			return todoList;
		}
		else{
			return null;
		}
	}
	
	public List<TodoDto> getFromDate(UserDto userDto, String stringFromDate){
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		Date fromDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			fromDate = formatter.parse(stringFromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		todoEntitys = (ArrayList<TodoEntity>) todoRepository.findByUserAndCreateDate(userEntity, fromDate);
				
		todoDtos = new ArrayList<TodoDto>();
		if(!todoEntitys.isEmpty()){
			for(int i = 0; i < todoEntitys.size(); i++){
				todoDto = modelMapper.map(todoEntitys.get(i), TodoDto.class);
				todoDtos.add(todoDto);
			}
			List<TodoDto> todoList = todoDtos;
			return todoList;
		}
		else{
			return null;
		}
	}
	
	public List<TodoDto> getToDate(UserDto userDto, String stringToDate){
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		Date toDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			toDate = formatter.parse(stringToDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		todoEntitys = (ArrayList<TodoEntity>) todoRepository.findByUserAndFinishDate(userEntity, toDate);
				
		todoDtos = new ArrayList<TodoDto>();
		if(!todoEntitys.isEmpty()){
			for(int i = 0; i < todoEntitys.size(); i++){
				todoDto = modelMapper.map(todoEntitys.get(i), TodoDto.class);
				todoDtos.add(todoDto);
			}
			List<TodoDto> todoList = todoDtos;
			return todoList;
		}
		else{
			return null;
		}
	}
}