
/*  --------------------------- Controller của Form thêm chủ phòng ---------------------------  
     Form này sẽ hiển khi ấn vào nút "Thêm khách"*/

package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import DAO.DAO_Member;
import DAO.DAO_Room;
import DAO.DAO_Tenant;
import application.Support;
import application.TitleForms;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Member;
import model.Tenant;

public class addTenantController implements Initializable{
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
	@FXML
	private Button backButton; // Nút quay lại
	@FXML
	private Button saveButton; // Nút lưu
    @FXML
	private Label getTownIDLabel; // Nơi hiển thị mã tòa
    @FXML
	private Label getRoomIDLabel; // Nơi hiển thị mã phòng
    @FXML
    private DatePicker BirthTenantDatePicker; // Nơi chọn ngày sinh
    @FXML
    private RadioButton FemaleTenantRadioButton; // Nơi chọn giới tính nữ của chủ phòng
    @FXML
    private TextField IDCitizenMemberTextField; // Nơi nhập căn cước thành viên phòng
    @FXML
    private TextField IDCitizenTenantTextField; // Nơi nhập căn cước chủ phòng
    @FXML
    private RadioButton MaleTenantRadioButton; // Nơi chọn giới tính nam của chủ phòng
    @FXML
    private RadioButton MemberFemaleRadionButton; // Nơi chọn giới tính nữ của thành viên phòng
    @FXML
    private RadioButton MemberMaleRadioButton; // Nơi chọn giới tính nữ của thành viên phòng
    @FXML
    private TextField MemberNameTextField; // Nơi nhập tên thành viên phòng
    @FXML
    private TextField NameTenantTextField; // Nơi nhập tên chủ phòng
    @FXML
    private TextField TenantIDTextField; // Nơi nhập mã chủ thuê phòng
    @FXML
    private DatePicker birthMemberDatePicker; // Nơi chọn ngày sinh thành viên phòng
    @FXML
    private TextField phoneNumMemberTextField; // Nơi nhập số điện thoại thành viên phòng
    @FXML
    private TextField phoneNumTenantTextField; // Nơi nhập số điện thoại chủ phòng
    @FXML
    private TextField placeMemberTextField; // Nơi nhập quê thành viên phòng
    @FXML
    private TextField placeTenantTextField; // Nơi nhập quê chủ phòng
    @FXML
    private DatePicker rentDatePicker; // Nơi chọn ngày thuê phòng
    @FXML
    private Tab TenantTab; // Tab chủ phòng
    @FXML
    private Tab MemberTab; // Tab thành viên
    @FXML
    private TableView<Member> MemberTableView; // Bảng thông tin thành viên
    @FXML
    private TableColumn<Member, Integer> order; // Cột số thứ tự 
    @FXML
    private TableColumn<Member, String> CitizenIDMember_col; // Cột căn cước
    @FXML
    private TableColumn<Member, String> MemberSex_col; // Cột giới tính
    @FXML
    private TableColumn<Member, String> NameMember_col; // Cột họ tên
    @FXML
    private TableColumn<Member, LocalDate> birthMember_col; // Cột ngày sinh
    @FXML
    private TableColumn<Member, String> phoneNumMember_col; // Cột số điện thoại
    @FXML
    private TableColumn<Member, String> placeMember_col; // Cột quê quán
    /*----------------------------------------------------------*/
    
    /*---------------------Khai báo hỗ trợ---------------------*/
    private boolean checkAddingCompleted; // Thuộc tính kiểm tra xem đã thêm chủ phòng chưa
    /*----------------------------------------------------------*/
    
    /*---------------------Phương thức chính của file--------------------*/
	/*---------------------------Tab chủ thuê phòng---------------------------*/
    
    // Phương thức kiểm tra phần nhập thông tin
    public boolean checkTenantEmpty() {
		if(TenantIDTextField.getText().isEmpty()
				|| NameTenantTextField.getText().isEmpty()
				|| (!MaleTenantRadioButton.isSelected() && !FemaleTenantRadioButton.isSelected())
				|| BirthTenantDatePicker.getValue() == null
				|| IDCitizenTenantTextField.getText().isEmpty()
				|| phoneNumTenantTextField.getText().isEmpty()
				|| placeTenantTextField.getText().isEmpty()
				|| rentDatePicker.getValue() == null) {
			return true;
		}
		return false;
	}
    
    // Phương thức khóa phần nhập thông tin sau khi thêm chủ phòng
    public void lockInfor() {
		TenantIDTextField.setEditable(false);
		
		NameTenantTextField.setEditable(false);
		
		MaleTenantRadioButton.setDisable(true);
		FemaleTenantRadioButton.setDisable(true);
		
		BirthTenantDatePicker.setDisable(true);		

		IDCitizenTenantTextField.setEditable(false);

		phoneNumTenantTextField.setEditable(false);
		
		placeTenantTextField.setEditable(false);

		rentDatePicker.setDisable(true);
    }
    
    // Phương thức thêm chủ phòng (Nút "Lưu")
	public void addTenant() {
		/* Kiểm tra xem nếu đã thêm chủ phòng thì nút Lưu sẽ chuyển sang 
		 * sử dụng cho Tab thêm thành viên */
		if(checkAddingCompleted) {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", null, "Bạn đã thêm chủ phòng rồi !"
					+ "\nHay sang phần Thành Viên để sử dụng chức năng này !");
		} else {
			// Kiểm tra thông tin nhập
			if(checkTenantEmpty()) {
				Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Vui lòng điền đầy đủ thông tin!");
			}
			else {
				// Tạo biến kiểm tra trùng mã khách và căn cước công dân
				boolean duplicatedTenantID = false, duplicatedTenantCitizenID = false;
				for(Tenant x : Support.tenantList) {
					if(x.getTenantID().equalsIgnoreCase(TenantIDTextField.getText())) {
						duplicatedTenantID = true;
						break;
					}
				}
				
				for(Tenant x1 : Support.tenantList) {
					if(!x1.getCitizenID().isEmpty() && IDCitizenMemberTextField.getText().isEmpty() 
							&& x1.getCitizenID().equals(IDCitizenTenantTextField.getText())) {
						duplicatedTenantCitizenID = true;
						break;
					}
				}
				// Nếu không trùng
				if(!duplicatedTenantID && !duplicatedTenantCitizenID) {
					String sex = "";
					if(MaleTenantRadioButton.isSelected()) {
						sex = MaleTenantRadioButton.getText();
					}
					else {
						sex = FemaleTenantRadioButton.getText();
					}
					// Tạo đối tượng tương ứng với phần cần thêm thông tin
					Tenant tenant = new Tenant(TenantIDTextField.getText(),
							NameTenantTextField.getText(), sex,
							BirthTenantDatePicker.getValue(),
							IDCitizenTenantTextField.getText(),
							phoneNumTenantTextField.getText(),
							placeTenantTextField.getText(),
							rentDatePicker.getValue());
					Support.tenantList.add(tenant);
					// Thực hiện truy vấn thêm thông tin
					DAO_Tenant.getInstance().insertData(tenant, getRoomIDLabel.getText(), getTownIDLabel.getText());
					lockInfor(); // Khóa phần nhập
					Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Thêm thông tin thành công !");
					checkAddingCompleted = true; // Cập nhật biến kiểm tra chủ phòng
				}
				else if(duplicatedTenantID) {
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Mã khách bị trùng !"
							+ "\nMã khách không phân biệt chữ hoa và chữ thường"
							+ "\nVui lòng nhập mã khách khác !");
				}
				else if(duplicatedTenantCitizenID) {
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Căn cước công dân bị trùng !");
				}
			}
		}
	}
	/*-------------------*/
	
	/*---------------------------Tab thành viên phòng---------------------------*/ 
	// Phương thức kiểm tra phần nhập thông tin
	public boolean checkMemberEmpty() {
		if(MemberNameTextField.getText().isEmpty()
				|| (!MemberMaleRadioButton.isSelected() && !MemberFemaleRadionButton.isSelected())
				|| birthMemberDatePicker.getValue() == null
				|| placeMemberTextField.getText().isEmpty()) {
			return true;
		}
		return false;
	}
	
	// Phương thức thêm thành viên
	public void addMember() {
		// Kiểm tra nhập thông tin
		if(checkMemberEmpty()) {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Vui lòng điền đầy đủ thông tin!");
		}
		else {
			int count = 1; // Thứ tự thành viên của phòng
			String sex = "", idMember = "";
			if(MemberMaleRadioButton.isSelected()) {
				sex = MemberMaleRadioButton.getText();
			}
			else {
				sex = MemberFemaleRadionButton.getText();
			}
			idMember = TenantIDTextField.getText() + "TV" + String.valueOf(count++);
			// Tạo đối tượng tương ứng
			Member member = new Member(idMember,
					MemberNameTextField.getText(), sex,
					birthMemberDatePicker.getValue(),
					IDCitizenMemberTextField.getText(),
					phoneNumMemberTextField.getText(),
					placeMemberTextField.getText());
			
			// Thêm đối tượng vào danh sách và thực hiện truy vấn cơ sở dữ liệu
			Support.currMemberList.add(member);	
			DAO_Member.getInstance().insertData(member, idMember, TenantIDTextField.getText());
			// Hiển thị thông tin thành viên lên bảng 
			MemberTableView.setItems(Support.currMemberList);
			Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Thêm thông tin thành công !");
		}
	}
	/*------------------------------*/
	
	/*--------------------------Phương thức chung của 2 Tab--------------------------*/
	// Phương thức lưu thông tin (Nút "Lưu")
	@FXML
	public void Save() {
		// Nếu Tab chủ phòng được chọn
		if(TenantTab.isSelected()) {
			addTenant();
			if(checkAddingCompleted) {
				MemberTab.setDisable(false);
			}
		}
		// Nếu Tab thành viên phòng được chọn
		else if(MemberTab.isSelected()) {
			addMember();
		}
	}
	
	// Phương thức quay lại Form khách
	@FXML
    public void Back(ActionEvent event) {
    	try {
    		Support.root = FXMLLoader.load(getClass().getResource("/designFXML/TenantDesign.fxml"));
    		Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    		Support.scene = new Scene(Support.root);
    		Support.stage.setScene(Support.scene);
    		Support.stage.setTitle(TitleForms.TitleHomePageForm);
    		Support.stage.show();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
	/*-------------------------------------------*/
	
	/*---------------------Phương thức mở các Form---------------------*/
	//Mở form phòng
	@FXML
	public void OpenHomePageForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/HomePageDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	@FXML
	public void OpenTenantForm(ActionEvent event) {
		try {
			TenantButton.setStyle("-fx-background-color:#137b9e");
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
	
	@FXML
	public void OpenServiceForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/ServiceDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle(TitleForms.TitleServiceForm);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	/* Phương thức mặc định được gọi tự động khi một Controller của FXML 
	  được tạo và liên kết với một tập tin FXML */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
		TenantButton.setStyle(Support.colorButton); // Chỉnh màu nút Phòng khi đang chạy Form
		
		checkAddingCompleted = false; 
		
		// Khi phòng chưa có chủ thuê thì không thêm vào Tab thêm thành viên phòng
		MemberTab.setDisable(true);	
		
		Support.currMemberList = FXCollections.observableArrayList();
		
		getRoomIDLabel.setText(cardRoomController.idRoom); // Lấy mã phòng
		getTownIDLabel.setText(DAO_Room.getInstance().selectID(getRoomIDLabel.getText(), "")); // Lấy mã tòa
		
		// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
		order.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(Support.currMemberList.indexOf(cellData.getValue()) + 1).asObject());
		// Thiết lập cột tên thành viên
		NameMember_col.setCellValueFactory(new PropertyValueFactory<Member, String>("name"));
		// Thiết lập cột giới tính thành viên
		MemberSex_col.setCellValueFactory(new PropertyValueFactory<Member, String>("sex"));
		// Thiết lập cột ngày sinh thành viên
		birthMember_col.setCellValueFactory(new PropertyValueFactory<Member, LocalDate>("birthdate"));
		// Thiết lập cột căn cước thành viên
		CitizenIDMember_col.setCellValueFactory(new PropertyValueFactory<Member, String>("citizenID"));
		// Thiết lập cột số điện thoại thành viên
		phoneNumMember_col.setCellValueFactory(new PropertyValueFactory<Member, String>("phoneNum"));
		// Thiết lập cột quê quán thành viên
		placeMember_col.setCellValueFactory(new PropertyValueFactory<Member, String>("placeOrigin"));
	}
}
