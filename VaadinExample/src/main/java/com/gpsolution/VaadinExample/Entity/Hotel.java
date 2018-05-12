package com.gpsolution.VaadinExample.Entity;

import java.time.LocalDate;
import java.util.Date;

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
import javax.persistence.Version;

@Entity
@Table(name="HOTEL")
public class Hotel{

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;

	@Version
	@Column(name="OPTLOCK")
	private Long optlock;
	
	@Column(name="NAME")
	private String name = "";

	@Column(name="ADDRESS")
	private String address = "";

	@Column(name="RATING")
	private Integer rating;

	@Column(name="OPERATES_FROM")
	private Long operatesFrom;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CATEGORY_ID")
	private Category category;
	
	@Column(name="URL")
	private String url;

	@Column(name="DESCRIPTION")
	private String description;

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public String toString() {
		return name + " " + rating +"stars " + address;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public Hotel(){

    }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOptlock() {
		return optlock;
	}

	public void setOptlock(Long optlock) {
		this.optlock = optlock;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getOperatesFrom() {
		return operatesFrom;
	}

	public void setOperatesFrom(Long operatesFrom) {
		this.operatesFrom = operatesFrom;
	}

	public Integer getRating() { return rating; }

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Category getCategory() {
		return category;
	}
	
	public Category getCategoryView() {
		if (category == null) {
			return new Category("No category");
		}
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDate getDate(){
		Date date = new Date(this.operatesFrom);
		@SuppressWarnings("deprecation")
		LocalDate localDate = LocalDate.of(1900 + date.getYear(),1 + date.getMonth(), date.getDate());
		return localDate;
	}

	public Hotel(Long id, String name, String address, Integer rating, Long operatesFrom, Category category, String url, String description) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.rating = rating;
		this.operatesFrom = operatesFrom;
		this.category = category;
		this.url = url;
		this.description = description;
	}
}