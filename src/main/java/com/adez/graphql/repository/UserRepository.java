package com.adez.graphql.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adez.graphql.model.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {	
	UserEntity findByEmail(String email);
	UserEntity findByEmailAndPassword(String email, String password);
}