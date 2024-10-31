package model;

public class Service {
	private HomeTown home; // Đối tượng tòa nhà
	private String IDService; // Mã dịch vụ
	private String NameService; // Tên dịch vụ
	private String TypeService; // Loại dịch vụ
	private String PriceService; // Giá dịch vụ
	
	// Hàm khởi tạo của lớp
	public Service(HomeTown home, String iDService, String nameService, String typeService, String priceService) {
		this.home = home;
		IDService = iDService;
		NameService = nameService;
		TypeService = typeService;
		PriceService = priceService;
	}
	
	// Getter và Setter của lớp
	public HomeTown getHome() {
		return home;
	}
	public void setHome(HomeTown home) {
		this.home = home;
	}
	public String getIDService() {
		return IDService;
	}
	public void setIDService(String iDService) {
		IDService = iDService;
	}
	public String getNameService() {
		return NameService;
	}
	public void setNameService(String nameService) {
		NameService = nameService;
	}
	public String getTypeService() {
		return TypeService;
	}
	public void setTypeService(String typeService) {
		TypeService = typeService;
	}
	public String getPriceService() {
		return PriceService;
	}
	public void setPriceService(String priceService) {
		PriceService = priceService;
	}
}
