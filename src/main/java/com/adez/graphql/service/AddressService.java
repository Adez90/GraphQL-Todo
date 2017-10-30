package com.adez.graphql.service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adez.graphql.model.dto.AddressDto;
import com.adez.graphql.model.entity.AddressEntity;
import com.adez.graphql.repository.AddressRepository;

@Service
public class AddressService {
	private static AddressRepository addressRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	AddressEntity addressEntity;
	AddressDto addressDto;
	
	ArrayList<AddressEntity> addressEntitys;
	ArrayList<AddressDto> addressDtos;
	
	@Autowired
	public void setAddressRepository(AddressRepository addressRepository){
		AddressService.addressRepository = addressRepository;
	}
	
	public AddressDto saveAddress(AddressDto addressDto){
		addressEntity = modelMapper.map(addressDto, AddressEntity.class);
		
		addressEntity = addressRepository.save(addressEntity);
		
		addressDto = modelMapper.map(addressEntity, AddressDto.class);
		
		return addressDto;
	}
}
