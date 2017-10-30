package com.adez.graphql.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adez.graphql.model.dto.CompanyDto;
import com.adez.graphql.model.entity.CompanyEntity;
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
}
