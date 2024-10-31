package model;

import java.time.LocalDate;

public class Person {
	protected String name, sex, citizenID, phoneNum, placeOrigin;
	protected LocalDate birthdate;
	
	public Person() {
		
	}
	
	public Person(String name, String sex, LocalDate birthdate, 
			String citizenID, String phoneNum, String placeOrigin) {
		this.name = name;
		this.sex = sex;
		this.birthdate = birthdate;
		this.citizenID = citizenID;
		this.phoneNum = phoneNum;
		this.placeOrigin = placeOrigin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCitizenID() {
		return citizenID;
	}

	public void setCitizenID(String citizenID) {
		this.citizenID = citizenID;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPlaceOrigin() {
		return placeOrigin;
	}

	public void setPlaceOrigin(String placeOrigin) {
		this.placeOrigin = placeOrigin;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
}
