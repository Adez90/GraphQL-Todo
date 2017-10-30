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

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name ="company")
public class CompanyEntity {
	@Id
	@Column(name="comp_id", updatable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String companyName;
	
	@ManyToOne(optional=true, fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="address_id", referencedColumnName="address_id")
	private AddressEntity companyAddress;
	
	private String companyHomePage;
	
	private String companyEmail;
	
	private String companyPhoneNumber;
	
	@Column(name="organisation_number", unique=true)
	@NotEmpty
	private String organisationNumber;	

	public CompanyEntity() {
	}
	
	public CompanyEntity(String name, AddressEntity address,
			String homePage, String email, String phoneNumber, String organisationNumber) {
		this.companyName = name;
		this.companyAddress = address;
		this.companyHomePage = homePage;
		this.companyEmail = email;
		this.companyPhoneNumber = phoneNumber;
		this.organisationNumber = organisationNumber;
	}
	
	public CompanyEntity(String name, String organisationNumber, String email, String phoneNumber) {
		this.companyName = name;
		this.organisationNumber = organisationNumber;
		this.companyEmail = email;
		this.companyPhoneNumber = phoneNumber;
	}

	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String name) {
		this.companyName = name;
	}

	public AddressEntity getcompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(AddressEntity address) {
		this.companyAddress = address;
	}

	public String getCompanyHomePage() {
		return companyHomePage;
	}

	public void setCompanyHomePage(String homePage) {
		this.companyHomePage = homePage;
	}
	
	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String email) {
		this.companyEmail = email;
	}

	public String getCompanyPhoneNumber() {
		return companyPhoneNumber;
	}

	public void setCompanyPhoneNumber(String phoneNumber) {
		this.companyPhoneNumber = phoneNumber;
	}

	public String getOrganisationNumber() {
		return organisationNumber;
	}

	public void setOrganisationNumber(String organisationNumber) {
		this.organisationNumber = organisationNumber;
	}

	@Override
	public String toString() {
		return "CompanyEntity [name=" + companyName + ", address=" + companyAddress + ", homePage=" 
				+ companyHomePage + ", email=" + companyEmail + ", phoneNumber="
				+ companyPhoneNumber + ", organisationNumber=" + organisationNumber + "]";
	}
}
