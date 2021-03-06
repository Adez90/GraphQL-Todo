package com.adez.graphql.model.dto;

public class AddressDto {
	private Long id;

	private String streetAddress;
	
	private int zipCode;

	private String city;

	private String country;
	
	private String countryCode;
	
	public AddressDto() {		
	}
	
	public AddressDto(String streetAddress, int zipCode, String city, String country, String countryCode) {
		this.streetAddress = streetAddress;
		this.zipCode = zipCode;
		this.city = city;
		this.country = country;
		this.countryCode = countryCode;
	}
	
	public AddressDto(String streetAddress, Integer zipCode, String city,
			String country) {
		this.streetAddress = streetAddress;
		this.zipCode = zipCode;
		this.city = city;
		this.country = country;
		countryCode = country.substring(0, Math.min(country.length(), 3));
	}

	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	@Override
	public String toString() {
		return "AddressDto [streetAddress=" + streetAddress + ", zipCode=" + zipCode + ", city=" + city 
				+ ", country=" + country + ", countryCode=" + countryCode + "]";
	}
}
