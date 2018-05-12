package com.gpsolution.VaadinExample.Entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="CATEGORY")
public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long category_id;
	
	@Version
	@Column(name="OPTLOCK")
	private Long optlock;
	
	@Column(name="TYPE")
	private String type;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE},	 mappedBy="category")
	private List<Hotel> hotels;
	
	public Long getId() {
		return category_id;
	}

	public void setId(Long id) {
		this.category_id = id;
	}

	public Long getOptlock() {
		return optlock;
	}

	public void setOptlock(Long optlock) {
		this.optlock = optlock;
	}

    public Category() {
    }

    public Category(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category that = (Category) o;
        return Objects.equals(type, that.type);
    }
    
	@Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
