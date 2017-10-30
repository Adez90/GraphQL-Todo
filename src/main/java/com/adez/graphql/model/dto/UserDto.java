package com.adez.graphql.model.dto;

import java.util.ArrayList;
import java.util.List;


public class UserDto {
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String homePage;
	
	private String phoneNumber;
	
	private AddressDto address;
	
	private List<DepartmentDto> departmentList;
	
	public UserDto() {
	}
	
	public UserDto(String firstName, String lastName, String email, String password, String homePage, String phoneNumber,
			AddressDto address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.homePage = homePage;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public UserDto(String firstName, String lastName, String email, String password, String homePage, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.homePage = homePage;
		this.phoneNumber = phoneNumber;
	}
	
	public UserDto(String email, String password, String firstName,
			String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public UserDto(String email) {
		this.email = email;
	}

	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getHomePage(){
		return homePage;
	}
	
	public void setHomePage(String homePage){
		this.homePage = homePage;
	}
	
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	public AddressDto getAddress(){
		return address;
	}
	
	public void setAddress(AddressDto address){
		this.address = address;
	}
	
	public List<DepartmentDto> getDepartments(){
		return departmentList;
	}
	
	public void setDepartments(List<DepartmentDto> departmentList){
		this.departmentList = departmentList;
	}
	
	public void addDepartment(DepartmentDto department){
		if(departmentList != null){
			departmentList.add(department);
		}
		else{
			departmentList = new ArrayList<DepartmentDto>();
			departmentList.add(department);
		}
	}

	@Override
	public String toString() {
		return "UserDto [Id=" + id + ", firstName=" + firstName + ", lastName=" + lastName +  ", email=" + email + 
				", homePage=" + homePage + ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
	}
}
