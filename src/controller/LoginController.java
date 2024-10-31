
/*  --------------------------- Controller của Form đăng nhập ---------------------------  */

package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AccountLogin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import DAO.DAO_Login;
import application.Support;
import application.TitleForms;

public class LoginController implements Initializable {
	
	/*---------------------Các controls chính---------------------*/
    @FXML
    private FontAwesomeIconView openEye; // Biểu tượng mở mắt -> Ấn vào sẽ hiển thị mật khẩu
    @FXML
    private FontAwesomeIconView closeEye; // Biểu tượng nhắm mắt -> Ấn vào sẽ tắt hiển thị mật khẩu 
	@FXML
    private Hyperlink forgotPasswordHyperLink; // Hỗ trợ thiết kế phần quên mật khẩu
	@FXML
	private Label labelpasswordLabel; // Tiêu đề có chữ "Mật khẩu:"
	@FXML
	private Label showpasswordLabel; // Nơi hiển thị mật khẩu được tham chiếu từ PasswordField
    @FXML
    private Button loginButton; // Nút đăng nhập 
    @FXML
    private Button closeSlideButton; // Nút đăng đóng ở phần chuyển cảnh khi ấn vào "Quên mật khẩu"
    @FXML
    private AnchorPane loginForm; // Form chứa phần nhập tài khoản, mật khẩu và các nút
    @FXML
    private AnchorPane slideForm; // Form chuyển động, nó sẽ trượt khi ấn quên mật khẩu
    @FXML
    private AnchorPane noticeForm; // Form thông báo khi ấn vào quên mật khẩu
    @FXML
    private PasswordField passwordPasswordField; // Nơi nhập mật khẩu
    @FXML
    private TextField userIDTextField; // Nơi nhập tài khoản
    
    /*---------------------Khai báo hỗ trợ---------------------*/
    private ObservableList<AccountLogin> listAccount; // Danh sách các tài khoản 
    /*----------------------------------------------------------------------------*/
    
    /*---------------------Phương thức chính của file--------------------*/
    // Phương thức dịch chuyển Form khi ấn quên mật khẩu
    @FXML
    public void switchForm(ActionEvent event) {
    	// Khởi tạo đối tượng TranslateTransition để tạo dịch chuyển Form
    	TranslateTransition slider = new TranslateTransition();
    	// Kiểm tra nguồn sự kiện có phải từ HyperLink 'Quên mật khẩu'
    	if(event.getSource() == forgotPasswordHyperLink) {
    		slider.setNode(slideForm); // Đặt Form chuyển cảnh vào slider
    		slider.setToX(382); // Chọn vị trí x cần chuyển đến
    		slider.setDuration(Duration.seconds(.5)); // Thời gian dịch chuyển
    		loginForm.setVisible(false); // Tắt hiển thị của bên Form đăng nhập
    		
    		// Sự kiện sẽ xảy ra khi slider chạy xong 
    		slider.setOnFinished((ActionEvent e) -> {
    			closeSlideButton.setVisible(true); // Hiển thị nút đóng lại Form 
    			noticeForm.setVisible(true); // Hiển thị Form thông báo
    		});
    		slider.play(); // Chạy slider
    	}
    	// Kiểm tra xem nguồn sự kiện có phải do nút 'Đóng'
    	else if (event.getSource() == closeSlideButton){
    		slider.setNode(slideForm); // Đặt Form chuyển cảnh vào slider
    		slider.setToX(0); // Chọn vị trí x cần chuyển đến
    		slider.setDuration(Duration.seconds(.5)); 
    		noticeForm.setVisible(false); // Thời gian dịch chuyển
    		
    		// Sự kiện sẽ xảy ra khi slider chạy xong 
    		slider.setOnFinished((ActionEvent e) -> {
    			closeSlideButton.setVisible(false); // Tắt nút "Đóng"
    			loginForm.setVisible(true); // Mở lại Form đăng nhập 
    		});
			slider.play(); // Chạy slider
    	}
    }
    
    // Phương thức lấy nội dung nhập từ chỗ mật khẩu để hiển thị
    @FXML
    public void passwordDisplay(KeyEvent event) {
    	// Ánh xạ nội dung tại phần mật khẩu sang Label hiển thị mật khẩu
    	showpasswordLabel.textProperty().bind(Bindings.concat(passwordPasswordField.getText()));
    }
    
	// Phương thức hiển thị mật khẩu (Nút "Mở mắt")
    @FXML
    public void showPassword(MouseEvent event) {
    	closeEye.setVisible(true); // Hiển thị biểu tượng nhắm mắt
    	openEye.setVisible(false); // Tắt hiển thị biểu tượng mở mắt
    	
    	labelpasswordLabel.setVisible(true); // Hiển thị tiêu đề có chữ "Mật khẩu"
    	showpasswordLabel.setVisible(true); // Mở nơi hiển thị mật khẩu
    	
    	// Ánh xạ nội dung tại phần mật khẩu sang Label hiển thị mật khẩu
    	showpasswordLabel.textProperty().bind(Bindings.concat(passwordPasswordField.getText()));
    }
    
    // Phương thức ẩn hiển thị mật khẩu
    @FXML
    public void hidePassword(MouseEvent event) {
    	closeEye.setVisible(false); // Tắt hiển thị biểu tượng nhắm mắt
    	openEye.setVisible(true); // Hiển thị biểu tượng mở mắt
    	
    	// Đóng các phần hiện mật khẩu
    	showpasswordLabel.setVisible(false);
    	labelpasswordLabel.setVisible(false);
    }
    
    // Phương thức xử lý đăng nhập khi người dùng ấn nút Enter
    @FXML
    public void LoginByEnterKey() {
    	// Bắt sự kiện khi phần nhập ID tài khoản khi đang được trỏ chuột đặt vào
    	userIDTextField.setOnKeyPressed(event -> {
    		// Nếu nút Enter được ấn khi con trỏ chuột đang đặt vào phần nhập ID tài khoản
    		if(event.getCode() == KeyCode.ENTER && userIDTextField.isFocused()) {
    			try {
					LoginByButton(); // Gọi hàm đăng nhập dùng nút ấn
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
    		}
    	});
    	// Bắt sự kiện khi phần nhập mật khẩu khi đang được trỏ chuột đặt vào
    	passwordPasswordField.setOnKeyPressed(event -> {
    		// Nếu nút Enter được ấn khi con trỏ chuột đang đặt vào phần nhập mật khẩu
    		if(event.getCode().getName().equals("Enter")) {
    			if(event.getCode() == KeyCode.ENTER && passwordPasswordField.isFocused()) {
        			try {
    					LoginByButton(); // Gọi hàm đăng nhập dùng nút ấn
    				} catch (ClassNotFoundException ex) {
    					ex.printStackTrace();
    				}
    			}
    		}
    	});
    }
    
    // Phương thức xử lý đăng nhập khi người dùng ấn nút "Đăng nhập" trên Form
    @FXML
    public void LoginByButton() throws ClassNotFoundException {
    	// Kiểm tra trống thông tin và có chứa kí tự khoảng trắng
    	if(userIDTextField.getText().isBlank() && passwordPasswordField.getText().isBlank()) {
    		Support.NoticeAlert(AlertType.ERROR,"Thông báo", null ,"Tài khoản/Mật khẩu không được để trống !");
    		userIDTextField.setText("");
    		passwordPasswordField.setText("");
    		userIDTextField.requestFocus();
    	}
    	// Kiểm tra trống/kí tự khoảng trắng của phần nhập tài khoản 
    	else if(userIDTextField.getText().isBlank() && passwordPasswordField.getText() != null) {
    		Support.NoticeAlert(AlertType.ERROR,"Thông báo", null ,"Tài khoản không được để trống !");
    		userIDTextField.requestFocus();
    	}
    	// Kiểm tra trống/kí tự khoảng trắng của phần nhập mật khẩu 
    	else if(userIDTextField.getText() != null && passwordPasswordField.getText().isBlank()) {
    		Support.NoticeAlert(AlertType.ERROR,"Thông báo", null ,"Mật khẩu không được để trống !");
    		passwordPasswordField.requestFocus();
    	}
    	else {
    		// Lấy tài khoản từ cơ sở dữ liệu thông qua lớp DAO_Login
    		listAccount.addAll(DAO_Login.getInstance().selectByCondition(
        			userIDTextField.getText(), passwordPasswordField.getText(), ""));
    		// Kiểm tra kích thước danh sách
        	if(listAccount.size() == 0) {
        		Support.NoticeAlert(AlertType.ERROR,"Thông báo", null ,"Tài khoản/Mật khẩu không hợp lệ !");
        		userIDTextField.requestFocus();
        	}
        	else {
        		
        		AccountLogin x = listAccount.get(0); // Lấy đối tượng đầu tiên của danh sách - phần tử duy nhất
        		Support.IDAccount = x.getUserID(); // Lấy ID tài khoản
    			Support.pointRank = x.getRank(); // Lấy điểm cấp bậc gói đăng kí
    			Support.NameAccount = x.getAccountname(); // Lấy tên chủ tài khoản
    			Support.rankAccount = x.checkRank(x.getRank()); // Lấy tên gói đăng kí 
    			loginButton.getScene().getWindow().hide(); // Đóng cửa sổ đăng nhập
    			try {
    				// Mở Form trang chủ
    				Support.root = FXMLLoader.load(getClass().getResource("/designFXML/HomePageDesign.fxml"));
    				Support.stage = new Stage();
    				Support.scene = new Scene(Support.root);
    				Support.stage.setScene(Support.scene);
        			Support.stage.getIcons().add(Support.icon);
        			Support.stage.setTitle(TitleForms.TitleHomePageForm);
                    Support.stage.show();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
    	}
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	listAccount = FXCollections.observableArrayList(); // Khởi tạo danh sách tài khoản
	}
}
