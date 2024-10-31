package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


import DAO.DAO_Bill;
import DAO.DAO_Room;
import DAO.DAO_Service;
import DAO.DAO_Tenant;
import application.Support;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.Bill;
import model.HomeTown;
import model.Room;

public class calculateBillController implements Initializable{
	@FXML 
	private TextField electricNumberTextField; // Nơi nhập số điện
	@FXML
	private ChoiceBox<String> HomeTownChoiceBox; // Hộp chọn tòa nhà
	@FXML
	private ChoiceBox<String> roomChoiceBox; // Hộp chọn phòng
	@FXML
	private DatePicker invoiceDatePicker; // Nơi chọn ngày thuê
	@FXML
	private Label IDAccountLabel; // Hiển thị ID tài khoản xuất hóa đơn
	
	private ObservableList<Room> rentedRoomList;
	private Bill bill;
	private String price;
	ArrayList<String> arrRoomList;
	
	// Phương thức kiểm tra chuỗi nhập giá tiền dịch vụ hợp lệ
	public boolean isValidIndex(String price) {
			/*Sử dụng biểu thức chính quy để kiểm tra chuỗi
		 	\\d+: Kiểm tra xem có phải số tự nhiên không âm */
		if(price.matches("\\d+")) {
			int temp = Integer.parseInt(price); // Chuyển số tự nhiên không âm về chuỗi
			if(temp >= 0) return true;
		}
	    return false;
	}
	
	public void setRoomId(ActionEvent event) {
		arrRoomList.clear();
		rentedRoomList.clear();
		roomChoiceBox.getItems().clear();
		rentedRoomList.addAll(DAO_Room.getInstance().
				selectByCondition(HomeTownChoiceBox.getValue(), "1", null));
		for(Room x : rentedRoomList) {
			arrRoomList.add(x.getRoomID());
		}
		for(Bill x : Support.billList) {
			if(arrRoomList.contains(x.getRoomID())) {
				arrRoomList.remove(x.getRoomID());
			}
		}
	
		for(String id : arrRoomList) {
			roomChoiceBox.getItems().add(id);
		}
	}
	
	public String getAllPrice() {
		if(isValidIndex(electricNumberTextField.getText())) {
			if(HomeTownChoiceBox.getValue() == null 
					&& roomChoiceBox.getValue() == null) {
				return "0";
			}
			else {
				/* công thức tính tiền :  
				 tiền tổng = tổng các dịch vụ (không tính tiền điện) + tiền điện * số điện + tiền phòng */
				String anotherPriceService = DAO_Service.getInstance().sumPrice(HomeTownChoiceBox.getValue(), "!ĐIỆN");
				BigDecimal electricPriceService = new BigDecimal(
						DAO_Service.getInstance().sumPrice(HomeTownChoiceBox.getValue(), "ĐIỆN"))
						.multiply(new BigDecimal(electricNumberTextField.getText()));
				BigDecimal priceRoom = new BigDecimal(
						DAO_Room.getInstance().sumPrice(HomeTownChoiceBox.getValue(), roomChoiceBox.getValue()));
				
				BigDecimal result = new BigDecimal(0);
				result = priceRoom.add(electricPriceService).add(new BigDecimal(anotherPriceService));
				return result.toString();
			}
		}
		return "-1";
	}
	
	@FXML
	public void calculate() {
		price = getAllPrice();
		if("-1".equals(price)) {
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", null, "Vui lòng nhập đúng số điện !");
		}
		else if("0".equals(price)) {
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", null, "Vui lòng chọn đầy đủ thông tin !");
		}
		else {
			String tenantID = DAO_Tenant.getInstance()
					.selectID(HomeTownChoiceBox.getValue(), roomChoiceBox.getValue());
			bill = new Bill(Support.IDAccount, 
					HomeTownChoiceBox.getValue(), 
					roomChoiceBox.getValue(), 
					tenantID,
					Integer.parseInt(electricNumberTextField.getText()),
							invoiceDatePicker.getValue(),
							price);
			
			Support.billList.add(bill);
			DAO_Bill.getInstance().insertData(bill, "", "");
			BillController.secondStage.close();
		}
	}
	
	// Phương thức quay lại Form khách (Nút "Quay về")
    @FXML
    public void CloseStage(ActionEvent event) {
    	BillController.secondStage.close();
    }
	
	// hàm thêm Form vào BillGridPane ở form CalCulateBillController 
	public void addPane(GridPane billGridPane) throws IOException {
		if(bill != null) { // Điều kiện: Bill phải khác rỗng mới được thêm vào
			int row = Support.billList.size();
			FXMLLoader loader = new FXMLLoader() ;
			loader.setLocation(getClass().getResource("/designFXML/cardBill.fxml"));
			AnchorPane pane = loader.load();
			cardBillController calculateForm = loader.getController();
			calculateForm.setData(bill);
			billGridPane.add(pane, 0, row++);
		}
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		IDAccountLabel.setText(Support.IDAccount);
		arrRoomList = new ArrayList<String>();
		bill = null;

		invoiceDatePicker.setValue(LocalDate.now());
		rentedRoomList = FXCollections.observableArrayList();
		
		
		for (HomeTown x : Support.homeList) {
			HomeTownChoiceBox.getItems().addAll(x.getTownID());
		}
		HomeTownChoiceBox.setOnAction(this::setRoomId);
	}
}
