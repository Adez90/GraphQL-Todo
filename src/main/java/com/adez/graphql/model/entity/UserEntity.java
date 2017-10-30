package com.adez.graphql.model.entity;

import javax.persistence.CascadeType;
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
@Table(name ="users")
public class UserEntity {
	@Id
	@Column(name="users_id", updatable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String firstName;
	
	@Column
	private String lastName;
	
	@Column(unique=true, nullable=false) 
	@NotNull
	private String email;
	
	@Column
	private String password;
	
	private String homePage;
	
	private String phoneNumber;
	
	@ManyToOne(optional=true, fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="address_id", referencedColumnName="address_id")
	private AddressEntity address;
	
	public UserEntity() {
	}
	
	public UserEntity(String firstName, String lastName, String email,
			String password, String homePage, String phoneNumber,
			AddressEntity address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		if(password == null)
			password = "";
		this.password = password;
		this.homePage = homePage;
		this.phoneNumber = phoneNumber;
		this.address = address;
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		password.toLowerCase();
		this.password = password;
	}
	
	public void setEncodedPassword(String password) {
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
	
	public AddressEntity getAddress(){
		return address;
	}
	
	public void setAddress(AddressEntity address){
		this.address = address;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + " firstName=" + firstName + ", lastName=" + ", email=" + email + ", password=" + password +
				", homePage=" + homePage + ", phoneNumber=" + phoneNumber + ", address" + address + "]";
	}
}
