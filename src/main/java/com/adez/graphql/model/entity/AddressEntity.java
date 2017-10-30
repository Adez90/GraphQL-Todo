package com.adez.graphql.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name ="address")
public class AddressEntity {
	@Id
	@Column(name="address_id", updatable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false) 
	@NotNull
	private String streetAddress;
	
	@Column(nullable=false) 
	@NotNull
	private int zipCode;
	
	@Column(nullable=false) 
	@NotNull
	private String city;
	
	@Column(nullable=false) 
	@NotNull
	private String country;
	
	private String countryCode;
	
	public AddressEntity() {		
	}
	
	public AddressEntity(String streetAddress, int zipCode, String city, String country, String countryCode) {
		this.streetAddress = streetAddress;
		this.zipCode = zipCode;
		this.city = city;
		this.country = country;
		this.countryCode = countryCode;
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
		return "AddressEntity [streetAddress=" + streetAddress + ", zipCode=" + zipCode + ", city=" + city 
				+ ", country=" + country + ", countryCode=" + countryCode + "]";
	}
}
