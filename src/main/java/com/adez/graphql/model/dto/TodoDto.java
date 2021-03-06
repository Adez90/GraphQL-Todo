package com.adez.graphql.model.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoDto {
	Long id;
	
	String title;
	
	String description;
	
	String createDate;
	
	String finishDate;
	
	Boolean isDone;
	
	UserDto user;
	
	public TodoDto(){
	}
	
	public TodoDto(String title, String description, String createDate, UserDto user){
		this.title = title;
		this.description = description;
		this.createDate = createDate;
		this.isDone = false;
		this.user = user;
	}
	
	public TodoDto(String title, String description, String createDate, String finishDate, Boolean isDone, UserDto user){
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

	public String getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setCreateDate(Date createDate) {
		if(createDate != null){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.createDate = formatter.format(createDate);
		}
		else
			this.createDate = null;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	
	public void setFinishDate(Date finishDate) {
		if(finishDate != null){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.finishDate = formatter.format(finishDate);
		}
		else
			this.finishDate = null;
	}

	public Boolean getIsDone() {
		return isDone;
	}

	public void setIsDone(Boolean isDone) {
		this.isDone = isDone;
	}
	
	public UserDto getUser(){
		return user;
	}
	
	public void setUser(UserDto user){
		this.user = user;
	}

	@Override
	public String toString() {
		return "TodoDto [id=" + id + ", title=" + title + ", description="
				+ description + ", createDate=" + createDate + ", finishDate="
				+ finishDate + ", isDone=" + isDone + "]";
	}
}
