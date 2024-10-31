
/*  --------------------------- Controller của Form dịch vụ ---------------------------  */

package controller;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import DAO.DAO_Service;
import application.Support;
import application.TitleForms;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.HomeTown;
import model.Service;

public class ServiceController implements Initializable{
	/*---------------------Các controls cố định của chương trình chính---------------------*/
	// Các đối tượng đại diện cho các nút
	@FXML
	private Button HomeButton; // Nút ấn trang chủ
	@FXML
	private Button RoomButton; // Nút ấn phòng
	@FXML
	private Button TenantButton; // Nút ấn khách
	@FXML
	private Button ServiceButton; // Nút ấn dịch vụ
	@FXML
	private Button BillButton; // Nút ấn hóa đơn
	@FXML
	private Button StatisticsButton; // Nút ấn thống kê
	@FXML
	private Label accountNameLabel; // Hiển thị tên người dùng tài khoản
	@FXML
	private Button LogoutButton; // Nút ấn đăng xuất
	/*----------------------------------------------------------------------------*/
	
	/*---------------------Các controls chính---------------------*/
	// 1. Phần nhập thông tin
    @FXML
    private TextField ServiceIDTextField; // Nơi nhập mã dịch vụ
    @FXML
    private TextField ServiceNameTextField; // Nơi nhập tên dịch vụ
    @FXML
    private TextField ServicePriceTextField;  // Nơi nhập giá dịch vụ
    @FXML
    private ComboBox<String> chooseTownComboBox; // Hộp chọn tòa nhà để xem dịch vụ của tòa đó
    @FXML
    private ComboBox<String> chooseTownComboBox1; // Hộp chọn mã tòa để thêm dịch vụ vào tòa đó
    @FXML
    private ComboBox<String> chooseTypeService; // Hộp chọn loại dịch vụ
    
    // 2. Phần bảng
    @FXML
    private TableView<Service> ServiceTableView; // Bảng thông tin tất cả dịch vụ của các tòa
    @FXML
    private TableColumn<Service, Integer> ordinalService; // Cột số thứ tự 
    @FXML
    private TableColumn<Service, String> HomeTownID_col; // Cột mã tòa 
    @FXML
    private TableColumn<Service, String> NameService_col; // Cột tên dịch vụ
    @FXML
    private TableColumn<Service, String> PriceService_col; // Cột giá dịch vụ
    @FXML
    private TableColumn<Service, String> ServiceID_col; // Cột mã dịch vụ
    @FXML
    private TableColumn<Service, String> TypeService_col; // Cột loại dịch vụ
    /*-----------------------------------------------------------------------------------------------*/
    
    /*---------------------Khai báo hỗ trợ---------------------*/
	// Danh sách sử dụng cho việc kiểm tra tòa nhà có tồn tại phòng hay không
	private ObservableList<Service> listTownService; 
	/*----------------------------------------------------------------------------*/
	
    /*---------------------Phương thức chính của file--------------------*/
	// Phương thức kiểm tra phần nhập thông tin dịch vụ xem có trống không
	public boolean checkServiceEmpty() {
		if(chooseTownComboBox1.getValue() == null
				|| ServiceIDTextField.getText().isEmpty()
				|| ServiceNameTextField.getText().isEmpty()
				|| chooseTypeService.getValue() == null
				|| ServicePriceTextField.getText().isEmpty()) {
			return true;
		}
		return false;
	}
	
	// Phương thức kiểm tra chuỗi nhập giá tiền dịch vụ hợp lệ
	public boolean isValidPrice(String price) {
		/*Sử dụng biểu thức chính quy để kiểm tra chuỗi
			\\d*: Kiểm tra số trước dấu . có 0 hoặc nhiều chữ số.
			\\.?: Kiểm tra xem có thể có dấu . hoặc không có
	 		0*$:  Kiểm tra xem số sau dấu . phải là số 0 hoặc nhiều số 0
	 		\\d+: Kiểm tra xem có phải số tự nhiên không âm */
		if(price.matches("\\d*\\.?0*$")) {
			double temp1 = Double.parseDouble(price); // Chuyển số thực về chuỗi
			if(temp1 >= 0) return true;
		}
		else if(price.matches("\\d+")) {
			int temp = Integer.parseInt(price); // Chuyển số tự nhiên không âm về chuỗi
			if(temp >= 0) return true;
		}
        return false;
    }
	
	// Phương thức chuyển định dạng giá tiền
    private String formatPrice(String price) {
    	// Chuyển chuỗi thành số nguyên
        double temp = Double.parseDouble(price);

        // Tạo đối tượng NumberFormat để định dạng số theo định dạng tiền tệ của Hoa Kỳ.
        NumberFormat formatNum = NumberFormat.getCurrencyInstance(Locale.US);

        // Định dạng số theo chuẩn tiền tệ Hoa Kì
        String formattedNum = formatNum.format(temp);

        // Trả về chuỗi đã định dạng, đã xóa kí tự dollar $
        return formattedNum.replace("$", "");
    }
	
    /*Phương thức chuyển ngược từ định dạng tiền về dạng số bình thường 
     khi ấn chọn dịch vụ trên bảng, số tiền từ định dạng tiền sẽ về số bình thường */
    public String parsePrice(String price) {
    	String temp = price.replace(",", ""); // Xóa dấu ',' của chuỗi
        int position = temp.indexOf("."); // Tìm vị trí dấu '.' trong chuỗi
        return temp.substring(0, position); // Lấy chuỗi từ đầu chuỗi đến trước vị trí dấu '.'
    }
    
	// Phương thức thiết lập thông tin từ danh sách tòa nhà lên bảng hiển thị
	public void setServiceToTable(ObservableList<Service> list) {
		// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
		ordinalService.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(list.indexOf(cellData.getValue()) + 1).asObject());
		
		//Hiện giá trị của thuộc tính 'townID' từ đối tượng HomeTown nằm trong đối tượng Service
		HomeTownID_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getHome().getTownID()));
		
		// Thiết lập hiển thị mã dịch vụ được lấy từ thuộc tính 'IDService' lên cột mã dịch vụ
		ServiceID_col.setCellValueFactory(new PropertyValueFactory<Service, String>("IDService"));
		
		// Thiết lập hiển thị tên dịch vụ được lấy từ thuộc tính 'nameService' lên cột mã dịch vụ
		NameService_col.setCellValueFactory(new PropertyValueFactory<Service, String>("nameService"));
		
		// Thiết lập hiển thị loại dịch vụ được lấy từ thuộc tính 'typeService' lên cột mã dịch vụ
		TypeService_col.setCellValueFactory(new PropertyValueFactory<Service, String>("typeService"));
		
		// Thiết lập hiển thị giá dịch vụ chuẩn tiền tệ thông qua hàm định dạng lên cột mã dịch vụ (
		PriceService_col.setCellValueFactory(cellData ->
        new SimpleStringProperty(formatPrice(cellData.getValue().getPriceService())));
		// Đặt dữ liệu từ danh sách lên bảng hiển thị dịch vụ
		ServiceTableView.setItems(list);
	}
	
	// Phương thức xem dịch vụ của tòa đang được chọn từ hộp chọn
	@FXML
	public void showServiceOfTown() {
		if(chooseTownComboBox.getValue() == null) {
			Support.NoticeAlert(AlertType.INFORMATION, "Thông báo", null, "Hãy chọn tòa nhà cần xem dịch vụ !");
		}
		else if("Tất cả".equals(chooseTownComboBox.getValue())) {
			// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
			setServiceToTable(Support.serviceList);
		}
		else {
			String town = chooseTownComboBox.getValue(); //Lấy lựa chọn tòa nhà từ hộp tòa nhà
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu '-' có trong chuỗi
			listTownService.clear();
			listTownService.addAll(DAO_Service.getInstance().selectByCondition(codeHomeTownID[0], "", ""));
			
			// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
			ordinalService.setCellValueFactory(cellData -> 
			new SimpleIntegerProperty(listTownService.indexOf(cellData.getValue()) + 1).asObject());
			ServiceTableView.setItems(listTownService);
		}
	}
	
	// Phương thức thêm dịch vụ (Nút ấn 'Thêm')
	@FXML
	public void addService() {
		if(checkServiceEmpty()) {
			// Thông báo lỗi
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", null, "Vui lòng điền đầy đủ thông tin !");
		} 
		else if(!isValidPrice(ServicePriceTextField.getText())) {
			// Thông báo lỗi
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", null, "Giá dịch vụ không hợp lệ !");
		}
		else {
			boolean duplicatedService = false; // Biến kiểm tra trùng mã dịch vụ
			// Nếu danh sách dịch vụ tổng quát có chứa dữ liệu thì mới kiểm tra trùng
			if(Support.serviceList.size() > 0) {
				for(Service x : Support.serviceList) {
					if(x.getIDService().equalsIgnoreCase(ServiceIDTextField.getText())
							&& x.getHome().getTownID().equals(chooseTownComboBox1.getValue())) {
						duplicatedService = true;
						break;
					}
				}
			}
			// Nếu không trùng mã dịch vụ
			if(!duplicatedService) {
				// Khởi tạo đối tượng dịch vụ mới
				Service x = new Service(new HomeTown(chooseTownComboBox1.getValue(), null), 
						ServiceIDTextField.getText(), 
						ServiceNameTextField.getText(), 
						chooseTypeService.getValue(), 
						ServicePriceTextField.getText());
				// Thêm đối tượng dịch vụ mới vào danh sách dịch vụ tổng quát
				Support.serviceList.add(x);
				
				// Truy vấn thêm dịch vụ vào cơ sở dữ liệu qua lớp DAO_Service
				DAO_Service.getInstance().insertData(x, chooseTownComboBox1.getValue(), null);
				
				// Đặt lại nội dung hiển thị lên bảng dịch vụ
				ServiceTableView.setItems(Support.serviceList);
				
				// Thông báo thêm thành công
				Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Thêm thông tin thành công !");
			}
			else {
				// Thông báo lỗi thêm phòng do trùng mã phòng
				Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Mã dịch vụ bị trùng !"
						+ "\nMã dịch vụ không phân biệt chữ hoa và chữ thường"
						+ "\nVui lòng nhập mã dịch vụ khác !");
			}
		}
	}
	
	// Phương thức lấy thông tin dịch vụ trên bảng và hiển thị lên khu vực nhập thông tin
	@FXML
	public void viewService() throws ParseException {
		// Lấy chỉ số của dòng đang được chọn trên bảng hiển thị tòa nhà
		int infoData = ServiceTableView.getSelectionModel().getSelectedIndex();
		// Nếu không có dòng nào được chọn
		if(infoData <= -1) {
			return;
		}
		// Đặt thông tin vào phần nhập tương ứng
		chooseTownComboBox1.setValue(HomeTownID_col.getCellData(infoData));
		ServiceIDTextField.setText(ServiceID_col.getCellData(infoData));
		ServiceNameTextField.setText(NameService_col.getCellData(infoData));
		chooseTypeService.setValue(TypeService_col.getCellData(infoData));
		ServicePriceTextField.setText(parsePrice(PriceService_col.getCellData(infoData)));
	}
	
	// Phương thức cập nhật dịch vụ (Nút ấn 'Cập nhật')
	@FXML
	public void updateService() {
		// Kiểm tra nội dung nhập xem có trống không
		if(checkServiceEmpty()) {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
					"LỖI CẬP NHẬT THÔNG TIN", "Vui lòng chọn thông tin cần sửa trên bảng !");
		}
		// Kiểm tra giá phòng hợp lệ
		else if(!isValidPrice(ServicePriceTextField.getText())) {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
					"LỖI CẬP NHẬT THÔNG TIN", "Vui lòng điền mệnh giá tiền hợp lệ !");
		}
		else {
			try {
				// Biến so sánh phần nhập mã tòa và mã tòa được lấy từ bảng
				boolean compareServiceID = false; 
				// Nếu danh sách phòng không rỗng thì mới so sánh
				if(Support.serviceList.size() > 0) {
					for(Service temp : Support.serviceList) {
						if(temp.getIDService().equals(ServiceIDTextField.getText())) {
							compareServiceID = true;
							break;
						}
					}
					// Nếu mà tòa ở phần nhập trùng với mã tòa được lấy ra từ phần cần chỉnh sửa
					if(compareServiceID) {
						// Khởi tạo đối tượng dịch vụ mới
						Service x = new Service(new HomeTown(chooseTownComboBox1.getValue(), null), 
								ServiceIDTextField.getText(), 
								ServiceNameTextField.getText(), 
								chooseTypeService.getValue(), 
								ServicePriceTextField.getText());
						
						// Cập nhật dịch vụ lên cơ sở dữ liệu qua lớp DAO_Service
						DAO_Service.getInstance().updateData(
								x, ServiceIDTextField.getText(), chooseTownComboBox1.getValue());
					
						// Xóa danh sách dịch vụ tổng quát hiện tại do có sự thay đổi về dữ liệu 
						Support.serviceList.clear(); 
						
						// Lấy lại danh sách dịch vụ tổng quát từ cơ sở dữ liệu qua lớp DAO_Service
						Support.serviceList.addAll(DAO_Service.getInstance().selectALL());
						
						// Đặt lại nội dung hiển thị lên bảng dịch vụ
						setServiceToTable(Support.serviceList);
						
						// Thông báo sửa thành công
						Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", 
								null, "Sửa thông tin thành công !");
					}else {
						
						Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
								"LỖI SỬA THÔNG TIN", "Không thể sửa mã dịch vụ!");
					}
				}
				else {
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
							"LỖI SỬA THÔNG TIN", "Danh sách dịch vụ trống !"
							+ "\nVui lòng thêm dịch vụ !");
				}
			}catch (Exception e){
				e.printStackTrace();
					
			}
		}
	}
	
	// Phương thức xóa thông tin tại phần nhập (Nút ấn 'Tẩy')
	@FXML
	public void eraseServiceInTextField() {
		chooseTownComboBox1.setValue(null);
		chooseTypeService.setValue(null);
		ServiceIDTextField.setText("");
		ServiceNameTextField.setText("");
		ServicePriceTextField.setText("");
	}
	
	@FXML
	public void deleteService() {
		Service selection = ServiceTableView.getSelectionModel().getSelectedItem();
		if(selection == null) {
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", "LỖI XÓA THÔNG TIN", "Không thể xóa dịch vụ !"
	    			+ "\nVui lòng chọn phòng cần xóa trên bảng ");
		}
		else {
			DAO_Service.getInstance().deleteData(selection, selection.getHome().getTownID());
		    Support.serviceList.remove(selection);
		  	ServiceTableView.setItems(Support.serviceList);
		    Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Xóa thông tin thành công !");
		}
	}
	/*---------------------Phương thức mở các Form---------------------*/
	// ServiceController sẽ không có phương thức mở ServiceForm vì chính ServiceForm đang được chạy
	//Mở form trang chủ
	@FXML
	public void OpenHomePageForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/HomePageDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle(TitleForms.TitleHomePageForm);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Mở Form phòng
	@FXML
	public void OpenRoomForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/RoomDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle(TitleForms.TitleRoomForm);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Mở Form khách
	@FXML
	public void OpenTenantForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/TenantDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle(TitleForms.TitleTenantForm);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// Mở Form hóa đơn
	@FXML
	public void OpenBillForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/BillDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle(TitleForms.TitleBillForm);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Mở Form thống kê
	@FXML
	public void OpenStatisticsForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/StatisticsDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle(TitleForms.TitleStatisticsForm);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Đăng xuất
	@FXML
	public void Logout(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("THÔNG BÁO");
		alert.setHeaderText(null);
		alert.setContentText("Bạn có muốn đăng xuất ?");
		if(alert.showAndWait().get() == ButtonType.OK) {
			((Stage)LogoutButton.getScene().getWindow()).close();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/designFXML/LoginDesign.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setResizable(false);
			stage.getIcons().add(Support.icon); // Hiển thị icon lên cửa sổ
			stage.setTitle(TitleForms.TitleLoginForm); // Đặt tiêu đề cửa sổ
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			}
		}
	}
	/*------------------------------------------*/
	
	/* Phương thức mặc định được gọi tự động khi một Controller của FXML 
	  được tạo và liên kết với một tập tin FXML */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ServiceButton.setStyle(Support.colorButton); // Chỉnh màu nút Dịch vụ khi đang chạy Form
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
		
		Support.serviceList = FXCollections.observableArrayList();
		// Truy vấn danh sách dịch vụ từ cơ sở dữ liệu
		Support.serviceList.addAll(DAO_Service.getInstance().selectALL());
		setServiceToTable(Support.serviceList); // Hiển thị danh sách dữ liệu lên bảng 
		
		listTownService = FXCollections.observableArrayList();
		
		// Thêm dữ liệu vào hôp chọn loại dịch vụ 
		chooseTypeService.getItems().addAll("ĐIỆN", "NƯỚC", "KHÁC");
		
		// Thêm dữ liệu vào hôp chọn tòa nhà 
		chooseTownComboBox.getItems().add("Tất cả");
		for(HomeTown x : Support.homeList) {
			chooseTownComboBox.getItems().add(x.toString());
			chooseTownComboBox1.getItems().add(x.getTownID());
		}
	}
}
