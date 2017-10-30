package com.adez.graphql.repository;

import org.springframework.data.repository.CrudRepository;

import com.adez.graphql.model.entity.AddressEntity;


public interface AddressRepository extends CrudRepository<AddressEntity, Long>{

}
