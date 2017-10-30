package com.adez.graphql.model.dto;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDto {
	private Long id;
	
	private CompanyDto company;
	
	private String departmentName;
	
	private String departmentEmail;

	private String departmentHomePage;
	
	private String departmentPhoneNumber;
	
	private AddressDto departmentAddress;
	
	private List<UserDto> userList;

	public DepartmentDto() {
		
	}
	
	public DepartmentDto(CompanyDto company, String name, AddressDto address,
			String homePage, String email, String phoneNumber) {
		this.company = company;
		this.departmentName = name;
		this.departmentHomePage = homePage;
		this.departmentEmail = email;
		this.departmentPhoneNumber = phoneNumber;
		this.departmentAddress = address;
	}
	
	public DepartmentDto(String name, String email, String phoneNumber, String homePage, CompanyDto company) {
		this.company = company;
		this.departmentName = name;
		this.departmentEmail = email;
		this.departmentPhoneNumber = phoneNumber;
		this.departmentHomePage = homePage;
	}
	
	public DepartmentDto(String name, String email, String phoneNumber, CompanyDto company) {
		this.company = company;
		this.departmentName = name;
		this.departmentEmail = email;
		this.departmentPhoneNumber = phoneNumber;
	}
	
	public DepartmentDto(CompanyDto company, String name,
			AddressDto address) {
		this.company = company;
		this.departmentName = name;
		this.departmentAddress = address;
	}

	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String name) {
		this.departmentName = name;
	}
	
	public AddressDto getDepartmentAddress(){
		return departmentAddress;
	}
	
	public void setDepartmentAddress(AddressDto address){
		this.departmentAddress = address;
	}
	
	public String getDepartmentHomePage(){
		return departmentHomePage;
	}
	
	public void setDepartmentHomePage(String homePage){
		this.departmentHomePage = homePage;
	}
	
	public String getDepartmentEmail() {
		return departmentEmail;
	}

	public void setDepartmentEmail(String email) {
		this.departmentEmail = email;
	}
	
	public String getDepartmentPhoneNumber(){
		return departmentPhoneNumber;
	}
	
	public void setDepartmentPhoneNumber(String phoneNumber){
		this.departmentPhoneNumber = phoneNumber;
	}
	
	public CompanyDto getCompany() {
		return company;
	}

	public void setCompany(CompanyDto company) {
		this.company = company;
	}
	
	public List<UserDto> getUsers(){
		return userList;
	}
	
	public void setUsers(List<UserDto> userList){
		this.userList = userList;
	}
	
	public void addUser(UserDto user){
		if(userList != null){
			userList.add(user);
		}
		else{
			userList = new ArrayList<UserDto>();
			userList.add(user);
		}
	}

	@Override
	public String toString() {
		return "DepartmentDto [name=" + departmentName + ", address=" + departmentAddress + ", phoneNumber=" + departmentPhoneNumber 
				+ ", homePage=" + departmentHomePage + ", email=" + departmentEmail + ", phoneNumber=" + departmentPhoneNumber 
				+ "]";
	}
}
