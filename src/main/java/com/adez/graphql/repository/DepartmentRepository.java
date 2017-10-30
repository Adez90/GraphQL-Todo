package com.adez.graphql.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.adez.graphql.model.entity.DepartmentEntity;

@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {
	List<DepartmentEntity> findByDepartmentName(String name);
}