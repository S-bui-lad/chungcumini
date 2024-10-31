package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import DAO.DAO_Bill;
import DAO.DAO_Room;
import application.Support;
import javafx.application.Platform;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Bill;
import model.HomeTown;
import model.Room;

public class BillController implements Initializable{
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
    private GridPane billGridPane; // Nơi chứa dòng thông tin hóa đơn
    @FXML
    private ChoiceBox<String> HomeTownChoiceBox; // Hộp chọn tòa
    @FXML
    private DatePicker invoiceDatePicker; // Mục chọn ngày thuê
    @FXML
    private ChoiceBox<String> roomChoiceBox; // Hộp chọn phòng
	/*------------------------------------------------------------*/
    
    /*---------------------Khai báo hỗ trợ---------------------*/
    private ObservableList<Room> listrentedRoom; // Danh sách các phòng đã thuê
    private ObservableList<Bill> listStateBill; // Danh sách hóa đơn được lọc
	public static ObservableList<Node> lst ;// danh sách các node ứng với GridPane
	// khai báo dùng cho toàn cục
	public static Stage secondStage;
	
	/*---------------------Phương thức chính của file--------------------*/
	// Phương thức hiển thị danh sách hóa đơn đã có
	public void addMenuPane(ObservableList<Bill> list) {
		try {
			// Xóa ràng buộc từng hàng, cột để thiết lập dữ liệu hiển thị theo ý muốn
			billGridPane.getChildren().clear();
			billGridPane.getRowConstraints().clear();
			billGridPane.getColumnConstraints().clear();
			int row = 0;
			for(int q = 0; q < list.size(); q++) {
				FXMLLoader load = new FXMLLoader() ;
				load.setLocation(getClass().getResource("/designFXML/cardBill.fxml"));
				AnchorPane pane = load.load();
				cardBillController calculateForm = load.getController();
				calculateForm.setData(list.get(q));
				billGridPane.add(pane, 0, row++);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setRoomId(ActionEvent event) {
		listrentedRoom.clear();
		roomChoiceBox.getItems().clear();
		listrentedRoom.addAll(DAO_Room.getInstance().
				selectByCondition(HomeTownChoiceBox.getValue(), "1", ""));
		for(Room x : listrentedRoom) {
			roomChoiceBox.getItems().add(x.getRoomID());
		}
	}
	
	@FXML
	public void filter() {
		// Nếu danh sách lọc đã có dữ liệu từ trước thì xóa
		if(listStateBill.size() > 0) {
			listStateBill.clear();
		}
		if(HomeTownChoiceBox.getValue() != null && roomChoiceBox.getValue() !=null && invoiceDatePicker.getValue() != null) { 
			listStateBill.addAll(DAO_Bill.getInstance().selectByCondition(HomeTownChoiceBox.getValue()
					, roomChoiceBox.getValue(), String.valueOf(invoiceDatePicker.getValue())));
		}
		else if(HomeTownChoiceBox.getValue() != null && roomChoiceBox.getValue() !=null) { 
			listStateBill.addAll(DAO_Bill.getInstance().selectByCondition(HomeTownChoiceBox.getValue()
					, roomChoiceBox.getValue(), null));
		}
		else if(HomeTownChoiceBox.getValue() != null && invoiceDatePicker.getValue() != null) { 
			listStateBill.addAll(DAO_Bill.getInstance().selectByCondition(HomeTownChoiceBox.getValue()
					, null, String.valueOf(invoiceDatePicker.getValue())));
		}
		else if(HomeTownChoiceBox.getValue() != null) { 
			listStateBill.addAll(DAO_Bill.getInstance().selectByCondition(HomeTownChoiceBox.getValue()
					, null,null ));
		}
		else if(invoiceDatePicker.getValue() != null) {// nếu chỉ có điều kiện của ngày xuất hóa đơn
			listStateBill.addAll(DAO_Bill.getInstance().selectByCondition(null
					, null, String.valueOf(invoiceDatePicker.getValue())));
		}
		
		Platform.runLater(() -> {
			addMenuPane(listStateBill);
		});
	}
	
	@FXML
	public void refreshBill() {
		addMenuPane(Support.billList);
	}
	
	@FXML
	public void calculate(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/designFXML/calculateBillDesign.fxml"));
			Parent root = loader.load();
			calculateBillController ctrl = loader.getController();
			secondStage.setScene(new Scene(root));
			secondStage.setResizable(false);
			secondStage.setTitle("Tính tiền phòng");
			secondStage.showAndWait();
			ctrl.addPane(billGridPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
			Image icon = new Image("/imageIcon/icon_miniapartment.png");
			stage.getIcons().add(icon);
			stage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Đăng nhập");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
		BillButton.setStyle(Support.colorButton); // Chỉnh màu nút Phòng khi đang chạy Form
		
		secondStage = new Stage();
		secondStage.initStyle(StageStyle.UTILITY);
		
		listrentedRoom = FXCollections.observableArrayList();
		listStateBill =  FXCollections.observableArrayList();

		Support.billList = FXCollections.observableArrayList();
		// Lấy danh sách tất cả hóa đơn đã tồn tại
		Support.billList.addAll(DAO_Bill.getInstance().selectALL());
		
		addMenuPane(Support.billList); // Hiển thị danh sách hóa đơn
		
		// gán danh sách các node ứng với BillGridPane để tiện cho việc thêm,xóa các CalculateForm.
		lst = billGridPane.getChildren(); 
		
		// Lấy danh sách mã tòa và thêm vapf ChoiceBox
		for (HomeTown x : Support.homeList) {
			HomeTownChoiceBox.getItems().addAll(x.getTownID());
		}
		HomeTownChoiceBox.setOnAction(this:: setRoomId);
	}

}
