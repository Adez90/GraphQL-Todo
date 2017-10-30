package com.adez.graphql.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.adez.graphql.model.entity.TodoEntity;
import com.adez.graphql.model.entity.UserEntity;

public interface TodoRepository extends CrudRepository<TodoEntity, Long> {
	List<TodoEntity> findByUser(UserEntity user);

	List<TodoEntity> findByUserId(long id);
	
	@Query("FROM event WHERE users_id = ?1 AND crate_date BETWEEN ?2 AND ?3")
	List<TodoEntity> findByUserAndDates(Long userId, Date fromDate, Date toDate);
	
	@Query("FROM event WHERE users_id = ?1 AND crate_date BETWEEN ?2 AND ?3 AND isFinished = TRUE")
	List<TodoEntity> findByUserAndDatesAndIsFinished(Long userId, Date fromDate, Date toDate);
	
	@Query("FROM event WHERE users_id = ?1 AND crate_date > ?2")
	List<TodoEntity> findByUserAndCreateDate(@Param("user") UserEntity user, @Param("cDate") Date createDate);
	
	@Query("FROM event WHERE users_id = ?1 AND crate_date < ?2")
	List<TodoEntity> findByUserAndFinishDate(@Param("user") UserEntity user, @Param("fDate") Date finishDate);
}
