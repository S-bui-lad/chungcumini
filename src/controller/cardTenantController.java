
/*  --------------------------- Controller của Form xem thông tin chủ phòng ---------------------------  
 *  Form này sẽ hiển khi ấn vào nút "Xem khách"*/

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Member;
import model.Tenant;

public class cardTenantController implements Initializable{
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
	private Button addButton; // Nút thêm
	@FXML
	private Button updateButton; // Nút cập nhật
	@FXML
	private Button deleteButton; // Nút xóa
	@FXML
	private Button deleteAllButton; // Nút xóa tất cả
    @FXML
	private Label getTownIDLabel1;  // Nơi hiển thị mã tòa
	@FXML
	private Label getRoomIDLabel1; // Nơi hiển thị mã phòng
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
    private ObservableList<Tenant> tenant; // Danh sách khách thuê theo phòng
    private Tenant x; // Đối tượng chủ phòng
    /*---------------------------------------------------------*/
    
    /*---------------------Phương thức chính của file--------------------*/
	/*---------------------------Tab chủ thuê phòng---------------------------*/
    
    // Phương thức lấy ra đối tượng chủ phòng đang được chọn
    public Tenant getTenantInfor() {
		tenant = FXCollections.observableArrayList();
		tenant.addAll(DAO_Tenant.getInstance().selectByCondition(getRoomIDLabel1.getText(),
				getTownIDLabel1.getText(), ""));
		Tenant object = tenant.get(0);
		return object;
	}
	
	public void showTenantInfor() {
		x = getTenantInfor(); // Lấy ra đối tượng
		
		// Lấy các thông tin của chủ phòng và hiển thị
		TenantIDTextField.setText(x.getTenantID());
		TenantIDTextField.setEditable(false);
		NameTenantTextField.setText(x.getName());
		BirthTenantDatePicker.setValue(x.getBirthdate());

		if("Nam".equals(x.getSex())) {
			MaleTenantRadioButton.setSelected(true);
		} else {
			FemaleTenantRadioButton.setSelected(true);
		}
		
		IDCitizenTenantTextField.setText(x.getCitizenID());
		phoneNumTenantTextField.setText(x.getPhoneNum());
		placeTenantTextField.setText(x.getPlaceOrigin());
		rentDatePicker.setValue(x.getRentDate());
	}
	
    // Phương thức kiểm tra nội dung nhập
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
	
    // Phương thức thêm chủ phòng
    public void updateTenant() {
    	if(checkTenantEmpty()) {
    		Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI SỬA THÔNG TIN", "Vui lòng điền đầy đủ thông tin!");
		}
		else {
			String sex = "";
			if(MaleTenantRadioButton.isSelected()) {
				sex = MaleTenantRadioButton.getText();
			}
			else {
				sex = FemaleTenantRadioButton.getText();
			}
			// Lấy dữ liệu từ nơi nhập và truyền vào phương thức khởi tạo của đối tượng
			Tenant tenant = new Tenant(TenantIDTextField.getText(),
					NameTenantTextField.getText(), sex,
					BirthTenantDatePicker.getValue(),
					IDCitizenTenantTextField.getText(),
					phoneNumTenantTextField.getText(),
					placeTenantTextField.getText(),
					rentDatePicker.getValue());
			
			// Truy vấn thêm thông tin vào cơ sở dữ liệu
			DAO_Tenant.getInstance().updateData(tenant, TenantIDTextField.getText(), "");
			
			Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Sửa thông tin thành công !");
		}
    }
    
    // Phương thức quay lại Form khách (Nút "Quay về")
    @FXML
    public void Back(ActionEvent event) {
    	try {
    		Support.root = FXMLLoader.load(getClass().getResource("/designFXML/TenantDesign.fxml"));
    		Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    		Support.scene = new Scene(Support.root);
    		Support.stage.setScene(Support.scene);
    		Support.stage.setTitle(TitleForms.TitleTenantForm);
    		Support.stage.getIcons().add(Support.icon);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
    // Phương thức lưu thông tin cập nhật (Nút "Lưu")
	@FXML
	public void Save(ActionEvent event) {
		// Nếu Tab chủ phòng đang được chọn
		if(TenantTab.isSelected()) {
			updateTenant();
		}
		// Nếu Tab thành viên phòng đang được chọn
		else if (MemberTab.isSelected()){
			updateMember();
		}
	}
	
	/* Phương thức xóa hết thông tin tại mục nhập đồng thời khóa lại hết lại 
	 không cho người dùng thao tác với các nút liên quan đến thêm sửa xóa sau khi ấn vào 
	 nút xóa tất cả thông tin */
	public void clearInformation() {
		// Vô hiệu hóa các nút
		addButton.setDisable(true);
		updateButton.setDisable(true);
		updateButton.setDisable(true);
		deleteButton.setDisable(true);
		deleteAllButton.setDisable(true);
		
		// Vô hiệu hóa các mục nhập thông tin
		TenantIDTextField.setText("");
		TenantIDTextField.setEditable(false);
		
		NameTenantTextField.setText("");
		NameTenantTextField.setEditable(false);
		
		MaleTenantRadioButton.setSelected(false);
		MaleTenantRadioButton.setDisable(true);
		FemaleTenantRadioButton.setSelected(false);
		FemaleTenantRadioButton.setDisable(true);
		
		BirthTenantDatePicker.setValue(null);
		BirthTenantDatePicker.setDisable(true);		
		
		IDCitizenTenantTextField.setText("");
		IDCitizenTenantTextField.setEditable(false);
		
		phoneNumTenantTextField.setText("");
		phoneNumTenantTextField.setEditable(false);
		
		placeTenantTextField.setText("");
		placeTenantTextField.setEditable(false);
		
		rentDatePicker.setValue(null);
		rentDatePicker.setDisable(true);
		
		MemberNameTextField.setEditable(false);
		
		MemberMaleRadioButton.setSelected(false);
		MemberMaleRadioButton.setDisable(true);
		MemberFemaleRadionButton.setSelected(false);
		MemberFemaleRadionButton.setDisable(true);
		
		birthMemberDatePicker.setValue(null);
		birthMemberDatePicker.setDisable(true);
		
		IDCitizenMemberTextField.setEditable(false);
		
		phoneNumMemberTextField.setEditable(false);
		
		placeMemberTextField.setEditable(false);
	}
	/*----------------------------*/
	
	/*---------------------------Tab thành viên phòng---------------------------*/
	// Phương thức kiểm tra nội dung nhập
	public boolean checkMemberEmpty() {
		if(MemberNameTextField.getText().isEmpty()
				|| (!MemberMaleRadioButton.isSelected() && !MemberFemaleRadionButton.isSelected())
				|| birthMemberDatePicker.getValue() == null
				|| placeMemberTextField.getText().isEmpty()) {
			return true;
		}
		return false;
	}
	
	//  Phương thức thiết lập thông tin từ danh sách thành viên của phòng lên bảng hiển thị
	public void setMemberToTable(ObservableList<Member> list) {
		// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
		order.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(list.indexOf(cellData.getValue()) + 1).asObject());
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
		
		MemberTableView.setItems(list); // Hiển thị thông tin từ danh sách lên bảng
	}
	// Phương thức lấy thông tin phòng trên bảng và hiển thị lên khu vực nhập thông tin
	@FXML
	public void view() {
		// Lấy chỉ số của dòng đang được chọn trên bảng hiển thị phòng
		int infoData = MemberTableView.getSelectionModel().getSelectedIndex();
	   	if(infoData <= -1) {
	   		return;
	   	}
	   	MemberNameTextField.setText(NameMember_col.getCellData(infoData));
	   	if("Nam".equals(MemberSex_col.getCellData(infoData))) {
	   		MemberMaleRadioButton.setSelected(true);
	   	}
	   	else {
	   		MemberFemaleRadionButton.setSelected(true);
	   	}
	   	// Đặt thông tin vào phần nhập tương ứng
	   	birthMemberDatePicker.setValue(birthMember_col.getCellData(infoData));
	   	IDCitizenMemberTextField.setText(CitizenIDMember_col.getCellData(infoData));
	   	phoneNumMemberTextField.setText(phoneNumMember_col.getCellData(infoData));
	   	placeMemberTextField.setText(placeMember_col.getCellData(infoData));
	}
	
	// Phương thức thêm chủ phòng (Nút "Thêm")
	@FXML
	public void addMember() {
		if(MemberTab.isSelected()) {
			// Kiểm tra phần nhập có trống không
			if(checkMemberEmpty()) {
				Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
						"LỖI THÊM THÔNG TIN", "Vui lòng điền đầy đủ thông tin!");
			}
			else {
				boolean duplicatedMemberCitizenID = false; // Biến kiểm tra trùng mã căn cước
				// Kiểm tra trùng căn cước nếu danh sách thành viên đã có
				if(Support.currMemberList.size() > 0) {
					for(Member x : Support.currMemberList) {
						if(x.getCitizenID().equals(IDCitizenMemberTextField.getText())
								&& !x.getCitizenID().equals("")
								&& !IDCitizenMemberTextField.getText().equals("")) {
							duplicatedMemberCitizenID = true;
							break;
						}
					}
				}
				if(!duplicatedMemberCitizenID) {
					/*Lấy số thứ tự thành viên phòng từ danh sách, 
					 * nếu chưa có thì số thứ tự sẽ là 1*/
					int count = Support.currMemberList.size() + 1;
					String sex = "", idMember = "";
					if(MemberMaleRadioButton.isSelected()) {
						sex = MemberMaleRadioButton.getText();
					}
					else {
						sex = MemberFemaleRadionButton.getText();
					}
					// Mã thành viên được tự động tạo
					idMember = TenantIDTextField.getText() + "TV" + String.valueOf(count++);
					// Lấy dữ liệu từ nơi nhập và truyền vào phương thức khởi tạo của đối tượng
					Member member = new Member(idMember,
							MemberNameTextField.getText(), sex,
							birthMemberDatePicker.getValue(),
							IDCitizenMemberTextField.getText(),
							phoneNumMemberTextField.getText(),
							placeMemberTextField.getText());
					
					Support.currMemberList.add(member);	// Thêm vào danh sách thành viên hiện tại
					// Thực hiện truy vấn thêm thành viên
					DAO_Member.getInstance().insertData(member, idMember, TenantIDTextField.getText());	
					MemberTableView.setItems(Support.currMemberList);
					Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, 
							"Thêm thông tin thành công !");
				}
				else {
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
							"LỖI THÊM THÔNG TIN", "Căn cước công dân bị trùng !");
				}
			}
		}
		else {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
					"LỖI CHỨC NĂNG", "Chức năng chỉ dành cho thành viên phòng !");
		}
	}
	
	// Phương thức cập nhập thành viên (Nút "Cập nhật")
	@FXML
	public void updateMember() {
		// Kiểm tra phần nhập thông tin
		if(checkMemberEmpty()) {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
					"LỖI SỬA THÔNG TIN", "Vui lòng chọn thông tin cần sửa trên bảng !");
		}
		else {
			boolean duplicatedMemberCitizenID = false; // Biến kiểm tra trùng mã căn cước
			// Kiểm tra trùng căn cước nếu danh sách thành viên đã có, nếu chưa có thì không thể sửa
			if(Support.currMemberList.size() > 0) {
				for(Member x : Support.currMemberList) {
					if(x.getCitizenID().equals(IDCitizenMemberTextField.getText())
							&& !x.getCitizenID().equals("")
							&& !IDCitizenMemberTextField.getText().equals("")) {
						duplicatedMemberCitizenID = true;
						break;
					}
				}
				// Nếu không trùng căn cước
				if(!duplicatedMemberCitizenID) {
					Member selection = MemberTableView.getSelectionModel().getSelectedItem();
					String sex = "";
					String idMember = selection.getMemberID();
					if(MemberMaleRadioButton.isSelected()) {
						sex = MemberMaleRadioButton.getText();
					}
					else {
						sex = MemberFemaleRadionButton.getText();
					}
					// Tạo đối tượng tương ứng
					Member member = new Member(idMember,
							MemberNameTextField.getText(), sex,
							birthMemberDatePicker.getValue(),
							IDCitizenMemberTextField.getText(),
							phoneNumMemberTextField.getText(),
							placeMemberTextField.getText());
					// Thực hiện truy vấn và cập nhật lại danh sách
					DAO_Member.getInstance().updateData(member, idMember, "");
					Support.currMemberList.clear();	
					Support.currMemberList.addAll(
							DAO_Member.getInstance().selectByCondition(TenantIDTextField.getText(), "", ""));
					MemberTableView.setItems(Support.currMemberList);
					Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", 
							null, "Thêm thông tin thành công !");
				}
				else {
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
							"LỖI SỬA THÔNG TIN", "Căn cước công dân bị trùng !");
				}
			}
			else {
				Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", 
						"LỖI SỬA THÔNG TIN", "Danh sách thành viên trống !"
						+ "\nVui lòng thêm thành viên !");
			}
		}
	}
	
	// Phương thức xóa 1 thành viên (Nút "Loại bỏ")
	@FXML
	public void deleteMember() {
		// Lấy đối tượng từ lựa chọn trên bảng
		Member selection = MemberTableView.getSelectionModel().getSelectedItem();
	    if(selection == null) {
	    	Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", "LỖI XÓA THÔNG TIN", 
	    			"Không thể xóa thành viên !"
	    			+ "\nVui lòng chọn thành viên cần xóa trên bảng ");
	    }
	    else {
	    	// Xóa đối tượng ở danh sách và trên cơ sở dữ liệu
			Support.currMemberList.remove(selection);
			DAO_Member.getInstance().deleteData(selection, "");
			MemberTableView.setItems(Support.currMemberList); // Hiển thị lại danh sách
			Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Xóa thông tin thành công !");
	    }
	}
	
	// Phương thức xóa tất cả thông tin chủ phòng và thành viên của phòng (Nút "xóa toàn bộ")
	@FXML
	public void deleteAll() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("THÔNG BÁO");
		alert.setHeaderText(null);
		alert.setContentText("Bạn có muốn xóa tất cả thông tin của khách thuê phòng ?");
		if(alert.showAndWait().get() == ButtonType.OK) {
			
			//Xóa hết bảng thành viên
			DAO_Member.getInstance().deleteAllData(TenantIDTextField.getText());
			Support.currMemberList.clear();
			MemberTableView.setItems(Support.currMemberList);
			
			// Xóa thông tin chủ phòng khỏi cơ sở dữ liệu và danh sách
			DAO_Tenant.getInstance().deleteData(x, getRoomIDLabel1.getText());
			Support.tenantList.remove(x);
			clearInformation(); // Gọi phương thức khóa thông tin
			Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Xóa thông tin thành công !");
		}
		
	}
	/*-----------------------------------------------------------------------------*/

	/*---------------------Phương thức mở các Form---------------------*/
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
	
	// Mở Form dịch vụ
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
	
	/* Phương thức mặc định được gọi tự động khi một Controller của FXML 
	  được tạo và liên kết với một tập tin FXML */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
		TenantButton.setStyle(Support.colorButton); // Chỉnh màu nút Phòng khi đang chạy Form
		
		getRoomIDLabel1.setText(cardRoomController.idRoom); // Lấy mã phòng
		getTownIDLabel1.setText(DAO_Room.getInstance().selectID(getRoomIDLabel1.getText(), "")); // Lấy mã tòa
		
		showTenantInfor(); //Hiển thị thông tin khách của mã phòng và mã tòa
		
		// Lấy danh sách thành viên tương ứng với phòng
		Support.currMemberList = FXCollections.observableArrayList();
		Support.currMemberList.addAll(DAO_Member.getInstance().
				selectByCondition(TenantIDTextField.getText(), "", ""));
		
		setMemberToTable(Support.currMemberList); // Hiển thị danh sách thành viên phòng tương ứng
	}
}
