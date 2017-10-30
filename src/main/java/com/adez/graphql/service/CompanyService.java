package com.adez.graphql.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adez.graphql.model.dto.CompanyDto;
import com.adez.graphql.model.dto.UserDto;
import com.adez.graphql.model.entity.CompanyEntity;
import com.adez.graphql.model.entity.DepartmentEntity;
import com.adez.graphql.model.entity.UserEntity;
import com.adez.graphql.repository.CompanyRepository;

@Service
public class CompanyService {
	private static CompanyRepository companyRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	CompanyEntity companyEntity;
	CompanyDto companyDto;
	
	ArrayList<CompanyEntity> companyEntitys;
	ArrayList<CompanyDto> companyDtos;
	
	@Autowired
	public void setCompanyRepository(CompanyRepository companyRepository){
		CompanyService.companyRepository = companyRepository;
	}
	
	public CompanyDto save(CompanyDto companyDto){
		companyEntity = modelMapper.map(companyDto, CompanyEntity.class);
			
		companyEntity = companyRepository.save(companyEntity);
			
		companyDto = modelMapper.map(companyEntity, CompanyDto.class);
			
		return companyDto;
	}
	
	public CompanyDto getById(Long id){
		companyEntity = companyRepository.findOne(id);
		
		companyDto = modelMapper.map(companyEntity, CompanyDto.class);
		
		return companyDto;
	}
	
	public CompanyDto getByName(String name){
		if(companyRepository.findByCompanyName(name) == null)
			return null;
		
		companyEntity = companyRepository.findByCompanyName(name);
		
		companyDto = modelMapper.map(companyEntity, CompanyDto.class);
		
		return companyDto;
	}
	
	public ArrayList<CompanyDto> getByUser(UserDto userDto){
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		ArrayList <DepartmentEntity> departmentEntitys = (ArrayList<DepartmentEntity>) userEntity.getDepartments();
		
		companyEntitys = new ArrayList<CompanyEntity>();
		companyDtos = new ArrayList<CompanyDto>();
		
		if(departmentEntitys != null){
			for(int i = 0; i < departmentEntitys.size(); i++){
				companyEntitys.add(departmentEntitys.get(i).getCompany());
			}
			if(companyEntitys != null){
				for(int i = 0; i < companyEntitys.size(); i++){
					companyDto = modelMapper.map(companyEntitys.get(i), CompanyDto.class);
					companyDtos.add(companyDto);
				}
				return companyDtos;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}
}
