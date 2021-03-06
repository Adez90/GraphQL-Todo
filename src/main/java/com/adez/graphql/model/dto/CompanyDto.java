package com.adez.graphql.model.dto;


public class CompanyDto {
	private Long id;
	
	private String companyName;
	
	private AddressDto companyAddress;
	
	private String companyHomePage;
	
	private String companyEmail;
	
	private String companyPhoneNumber;
	
	private String organisationNumber;

	public CompanyDto() {
	}
	
	public CompanyDto(String name, AddressDto address,
			String homePage, String email, String phoneNumber, String organisationNumber) {
		this.companyName = name;
		this.companyAddress = address;
		this.companyHomePage = homePage;
		this.companyEmail = email;
		this.companyPhoneNumber = phoneNumber;
		this.organisationNumber = organisationNumber;
	}
	
	public CompanyDto(String companyName, String organisationNumber, AddressDto address, String email, String phoneNumber, String homePage) {
		this.companyName = companyName;
		this.organisationNumber = organisationNumber;
		this.companyAddress = address;
		this.companyEmail = email;
		this.companyPhoneNumber = phoneNumber;
		this.companyHomePage = homePage;
	}
	
	public CompanyDto(String companyName, String organisationNumber, String email, String phoneNumber, String homePage) {
		this.companyName = companyName;
		this.organisationNumber = organisationNumber;
		this.companyEmail = email;
		this.companyPhoneNumber = phoneNumber;
		this.companyHomePage = homePage;
	}
	
	public CompanyDto(String companyName, String organisationNumber, String email, String phoneNumber) {
		this.companyName = companyName;
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

	public AddressDto getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(AddressDto address) {
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
		return "CompanyDto [name=" + companyName + ", address=" + companyAddress + ", homePage=" + companyHomePage + ", email=" + companyEmail + ", phoneNumber="
				+ companyPhoneNumber + ", organisationNumber=" + organisationNumber + "]";
	}
}
