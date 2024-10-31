package model;

import java.time.LocalDate;
import java.util.Objects;

public class Tenant extends Person {
	private String tenantID; 
	private LocalDate rentDate;
	
	public Tenant() {
		
	}
	
	public Tenant(String tenantID, String name, String sex, LocalDate birthdate,
			String citizenID, String phoneNum, String placeOrigin, LocalDate rentDate) {
		super(name, sex, birthdate, citizenID, phoneNum, placeOrigin);
		this.tenantID = tenantID;
		this.placeOrigin = placeOrigin;
		this.rentDate = rentDate;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public LocalDate getRentDate() {
		return rentDate;
	}

	public void setRentDate(LocalDate rentDate) {
		this.rentDate = rentDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tenant other = (Tenant) obj;
		return Objects.equals(birthdate, other.birthdate) && Objects.equals(citizenID, other.citizenID)
				&& Objects.equals(tenantID, other.tenantID) && Objects.equals(name, other.name)
				&& Objects.equals(phoneNum, other.phoneNum) && Objects.equals(placeOrigin, other.placeOrigin)
				&& Objects.equals(rentDate, other.rentDate) && Objects.equals(sex, other.sex);
	}
}

