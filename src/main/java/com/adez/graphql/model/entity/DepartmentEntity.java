package com.adez.graphql.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import se.up2u.flowkeeper.model.entity.CompanyEntity;

@Entity
@Table(name ="department")
public class DepartmentEntity {
	@Id
	@Column(name="department_id", updatable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="company_id", referencedColumnName="comp_id")
	private CompanyEntity company;
	
	@Column(nullable=false) 
	@NotNull
	private String departmentName;

	private String departmentEmail;

	private String departmentHomePage;
	
	private String departmentPhoneNumber;
	
	@ManyToOne(optional=true, fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="address_id", referencedColumnName="address_id")
	private AddressEntity departmentAddress;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="department_user_detail",
				joinColumns=@JoinColumn(name="department_id", referencedColumnName="department_id"),
				inverseJoinColumns=@JoinColumn(name="users_id", referencedColumnName="users_id"))
	private List<UserEntity> userList;

	public DepartmentEntity() {
		
	}
	
	public DepartmentEntity(String name, AddressEntity address,
			String homePage, String email, String phoneNumber) {
		this.departmentName = name;
		this.departmentHomePage = homePage;
		this.departmentEmail = email;
		this.departmentPhoneNumber = phoneNumber;
		this.departmentAddress = address;
	}
	
	public DepartmentEntity(String name, String email, String phoneNumber) {
		this.departmentName = name;
		this.departmentEmail = email;
		this.departmentPhoneNumber = phoneNumber;
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
	
	public AddressEntity getDepartmentAddress(){
		return departmentAddress;
	}
	
	public void setDepartmentAddress(AddressEntity address){
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
	
	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}
	
	public List<UserEntity> getUsers(){
		return userList;
	}
	
	public void setUsers(List<UserEntity> userList){
		this.userList = userList;
	}
	
	public void addUser(UserEntity user){
		userList.add(user);
	}

	@Override
	public String toString() {
		return "DepartmentEntity [name=" + departmentName + ", address=" + departmentAddress + ", phoneNumber=" + departmentPhoneNumber 
				+ ", homePage=" + departmentHomePage + ", email=" + departmentEmail + ", phoneNumber=" + departmentPhoneNumber + "]";
	}
}
