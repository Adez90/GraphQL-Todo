package com.adez.graphql.model.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name ="todo")
public class TodoEntity {
	@Id
	@Column(name="users_id", updatable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@Column(name="crate_date", nullable=false) 
	@NotNull
	private Date createDate;
	
	@Column(name="finish_date")
	private Date finishDate;
	
	@Column(name = "isDone")
	private Boolean isDone;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="users_id", referencedColumnName="users_id")
	private UserEntity user;
	
	public TodoEntity(){
	}
	
	public TodoEntity(String title, String description, Date createDate, UserEntity user){
		this.title = title;
		this.description = description;
		this.createDate = createDate;
		this.isDone = false;
		this.user = user;
	}
	
	public TodoEntity(String title, String description, Date createDate, Date finishDate, Boolean isDone, UserEntity user){
		this.title = title;
		this.description = description;
		this.createDate = createDate;
		this.finishDate = finishDate;
		this.isDone = isDone;
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String stringCreateDate) {
		if(stringCreateDate != null){
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				createDate = formatter.parse(stringCreateDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else
			createDate = null;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String stringFinishDate) {
		if(stringFinishDate != null){
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				finishDate = formatter.parse(stringFinishDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else
			finishDate = null;
	}

	public Boolean getIsDone() {
		return isDone;
	}

	public void setIsDone(Boolean isDone) {
		this.isDone = isDone;
	}
	
	public UserEntity getUser(){
		return user;
	}
	
	public void setUser(UserEntity user){
		this.user = user;
	}

	@Override
	public String toString() {
		return "TodoEntity [id=" + id + ", title=" + title + ", description="
				+ description + ", createDate=" + createDate + ", finishDate="
				+ finishDate + ", isDone=" + isDone + "]";
	}
}
