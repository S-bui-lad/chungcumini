package model;

public class RoomAndTenant {
	protected HomeTown home;
	protected Room room;
	protected Tenant tenant;
	
	public RoomAndTenant(HomeTown home, Room room, Tenant tenant) {
		super();
		this.home = home;
		this.room = room;
		this.tenant = tenant;
	}
	public HomeTown getHome() {
		return home;
	}
	public void setHome(HomeTown home) {
		this.home = home;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public Tenant getTenant() {
		return tenant;
	}
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
	
}
