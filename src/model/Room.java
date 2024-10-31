package model;

public class Room {
	private String roomID; // Mã phòng
	private String typeroom; // Loại phòng
	private String stateroom; // Trạng thái phòng
	private String priceroom; // Giá phòng
	
	// Phương thức khởi tạo có tham số
	public Room(String roomID, String typeroom, String stateroom, String priceroom) {
		this.roomID = roomID;
		this.typeroom = typeroom;
		this.stateroom = stateroom;
		this.priceroom = priceroom;
	}
	
	// Getter và Setter
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	public String getTyperoom() {
		return typeroom;
	}
	public void setTyperoom(String typeroom) {
		this.typeroom = typeroom;
	}
	public String getStateroom() {
		return stateroom;
	}
	public void setStateroom(String stateroom) {
		this.stateroom = stateroom;
	}
	public String getPriceroom() {
		return priceroom;
	}
	public void setPriceroom(String priceroom) {
		this.priceroom = priceroom;
	}
}
