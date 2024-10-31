package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Support;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UpgradePackController implements Initializable{
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
	@FXML
	private Label namePackServiceLabel; // Nơi hiển thị gói tài khoản
	@FXML
    private FontAwesomeIconView iconPack; // Biểu tượng của loại gói tài khoả
	/*---------------------Khai báo hỗ trợ---------------------*/
	private String namepack; // Thuộc tính phụ để lưu tên của loại dịch vụ
	
	/*---------------------Phương thức chính của file--------------------*/
	// Phương thức hiển thị biểu tượng của loại gói tài khoản
	public void showNameAndIconPack(Label namepack,String nameicon, Color color) {
		namepack.setTextFill(color);
		iconPack.setGlyphName(nameicon);
		iconPack.setFill(color);
	}
	
	// Phương thức hiển thị loại gói tài khoản
	public void showServicePack(String namepack) {
		namePackServiceLabel.setText(namepack); // Hiển thị gói tài khoản
		if("Gói miễn phí".equals(namepack)) {
			showNameAndIconPack(namePackServiceLabel,"PUZZLE_PIECE", Color.web("#7F7F7F"));
		}
		else if("Gói cơ bản 1".equals(namepack)) {
			showNameAndIconPack(namePackServiceLabel, "CHECK", Color.web("#E28E42"));
		}
		else if("Gói cơ bản 2".equals(namepack)) {
			showNameAndIconPack(namePackServiceLabel, "PLUS_SQUARE", Color.web("#7030A0"));
		}
		else if("Gói cao cấp 1".equals(namepack)) {
			showNameAndIconPack(namePackServiceLabel, "STREET_VIEW", Color.web("#2E7C82"));
		}
		else if("Gói cao cấp 2".equals(namepack)) {
			showNameAndIconPack(namePackServiceLabel, "STAR", Color.web("#FF7979"));
		}
	}
	
	
	@FXML
	public void OpenHomePageForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/HomePageDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Trang chủ");
			Support.stage.getIcons().add(Support.icon);
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
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void OpenTenantForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/TenantDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Quản lý khách thuê phòng");
			Support.stage.getIcons().add(Support.icon);
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
			Support.stage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Quản lý dịch vụ");
			Support.stage.getIcons().add(Support.icon);
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
			Support.stage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Quản lý hóa đơn");
			Support.stage.getIcons().add(Support.icon);
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
			Support.stage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Thống kê");
			Support.stage.getIcons().add(Support.icon);
			Support.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Đăng xuất
	@FXML
	public void Logout() {
		// Thiết lập và hiển thị thông báo xác nhận đăng xuất
		Alert alert = new Alert(AlertType.CONFIRMATION); // Thông báo kiểu xác nhận
		alert.setTitle("THÔNG BÁO");
		alert.setHeaderText(null);
		alert.setContentText("Bạn có muốn đăng xuất ?");
			
		// Nếu người dùng ấn nút OK thì sẽ đóng cửa sổ hiện tại và mở lại cửa sổ đăng nhập
		if(alert.showAndWait().get() == ButtonType.OK) {
			((Stage)LogoutButton.getScene().getWindow()).close(); // Đóng cửa sổ chứa nút đăng xuất
			try {
				/* Tạo đối tượng FXMLLoader để lấy và chuyển đổi nội dung của file FXML
				từ đường dẫn thành cây chứa các nút của giao diện người dùng */
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/designFXML/LoginDesign.fxml"));
				Parent root = loader.load(); // Tải và khởi tạo nút gốc
				Stage stage = new Stage(); // Tạo đối tượng cửa sổ Stage mới
				Scene scene = new Scene(root); // Tạo đối tượng Scene
				stage.setScene(scene); // Đặt Scene và Stage
				stage.setResizable(false); // Không cho người dùng điều chỉnh kích thước cửa sổ
					
				// Tạo đối tượng Image chứa hình ảnh từ đường dẫn
				Image icon = new Image("/imageIcon/icon_miniapartment.png");
				stage.getIcons().add(icon); // Hiển thị icon lên cửa sổ
				stage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Đăng nhập"); // Đặt tiêu đề cửa sổ
				stage.show(); // Hiển thị cửa sổ
			} catch (IOException e) {
				// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
				e.printStackTrace();
			}
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		HomeButton.setStyle(Support.colorButton); // Chỉnh màu nút Trang chủ khi đang chạy Form
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
		namepack = Support.rankAccount; // Lấy thông tin gói tài khoản đang sử dụng
		showServicePack(namepack); // Gọi phương thức hiển thị gói tài khoản
	}
}
