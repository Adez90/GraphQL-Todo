package com.adez.graphql.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adez.graphql.model.dto.DepartmentDto;
import com.adez.graphql.model.entity.DepartmentEntity;
import com.adez.graphql.repository.DepartmentRepository;

@Service
public class DepartmentService {
	private static DepartmentRepository departmentRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	DepartmentEntity departmentEntity;
	DepartmentDto departmentDto;
	
	ArrayList<DepartmentEntity> departmentEntitys;
	ArrayList<DepartmentDto> departmentDtos;
	
	@Autowired
	public void setRepository(DepartmentRepository departmentRepository){
		DepartmentService.departmentRepository = departmentRepository;
	}
	
	public DepartmentDto save(DepartmentDto departmentDto){
		departmentEntity = modelMapper.map(departmentDto, DepartmentEntity.class);
			
		departmentEntity = departmentRepository.save(departmentEntity);
		
		departmentDto = modelMapper.map(departmentEntity, DepartmentDto.class);
		
		return departmentDto;
	}
	
	public DepartmentDto getById(Long id){
		departmentEntity = departmentRepository.findOne(id);
		
		departmentDto = modelMapper.map(departmentEntity, DepartmentDto.class);
		
		return departmentDto;
	}
	
	public DepartmentDto getByName(String name){
		departmentEntitys = new ArrayList<DepartmentEntity>();
		departmentEntitys = (ArrayList<DepartmentEntity>) departmentRepository.findByDepartmentName(name);
		if(departmentEntitys.isEmpty())
			return null;
		
		departmentEntity = departmentEntitys.get(0);
		
		departmentDto = modelMapper.map(departmentEntity, DepartmentDto.class);
		
		return departmentDto;
	}
}
