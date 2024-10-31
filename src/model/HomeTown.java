package model;

import java.util.Objects;

public class HomeTown {
	private String townID; // Mã tòa
	private String address; // Địa chỉ tòa
	
	// Phương thức khởi tạo có tham số
	public HomeTown(String townID, String address) {
		this.townID = townID;
		this.address = address;
	}
	
	// Getter và Setter
	public String getTownID() {
		return townID;
	}

	public void setTownID(String townID) {
		this.townID = townID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HomeTown other = (HomeTown) obj;
		return Objects.equals(address, other.address) && Objects.equals(townID, other.townID);
	}

	@Override
	public String toString() {
		return townID + " - Địa chỉ: " + address;
	}
}
