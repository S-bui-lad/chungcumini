
/*  ----------------------- Controller của phần ô hiển thị thông tin phòng -----------------------  
  Lớp này sẽ có 2 phương thức xử lý nút ấn Thêm khách và Xem thông tin phòng:
  + Nút Thêm khách: chạy addTenantController
  + Nút Xem thông tin phòng: Chạy cardTenantController */

package controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import application.Support;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Room;

public class cardRoomController implements Initializable {
	/*---------------------Các controls chính---------------------*/
	@FXML
    private Button addTenantButton; // Nút thêm khách
	@FXML
    private Button showTenantButton; // Nút xem khách
	@FXML
    private Label priceroomLable; // Nơi hiện giá phòng
    @FXML
    private Label roomIDLabel; // Nơi hiện mã phòng
    @FXML
    private Label stateroomLabel; // Nơi hiện trạng thái phòng
    @FXML
    private Label typeroomLabel; // Nơi hiện loại phòng
    /*------------------------------------------------------------*/
    
    /*---------------------Khai báo hỗ trợ---------------------*/
    private double priceRoom;
    public static String idRoom = "";
    /*---------------------------------------------------------*/
    
    /*---------------------Phương thức chính của file--------------------*/
    // Phương thức lấy dữ liệu từ phòng và hiển thị thông tin
    public void setRoom(Room room) {
    	roomIDLabel.setText(room.getRoomID());
    	typeroomLabel.setText(room.getTyperoom());
    	
    	String state = room.getStateroom();
    	// Nếu phòng đã được thuê thì nút xem khách sẽ được hiển thị
    	if("Đã thuê".equals(state)) {
    		addTenantButton.setVisible(false);
    		showTenantButton.setVisible(true);
    		stateroomLabel.setTextFill(Color.RED); // Màu chữ đỏ phần trạng thái phòng
    	}
    	else {
    		stateroomLabel.setTextFill(Color.BLUE); // Màu chữ xanh phần trạng thái phòng
    	}
    	stateroomLabel.setText(state);
    	// Chuyển dữ liệu tiền về dạng số 
    	priceRoom = Double.parseDouble(room.getPriceroom());
    	// Định dạng lại số tiền về dạng chuẩn tiền tệ
        priceroomLable.setText(NumberFormat.getNumberInstance(Locale.US).format(priceRoom));
    }
    
    // Phương thức thêm khách (Mở Form của addTenantController)
    @FXML
	public void addTenant(ActionEvent event) {
    	idRoom = roomIDLabel.getText();
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/addTenantDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
    // Phương thức xem khách (Mở Form của cardTenantController)
    @FXML
	public void showTenant(ActionEvent event) {
    	idRoom = roomIDLabel.getText();
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/cardTenantDesign.fxml"));
			Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
			Support.scene = new Scene(Support.root);
			Support.stage.setScene(Support.scene);
			Support.stage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
