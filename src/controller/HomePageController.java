
/*  --------------------------- Controller của Form trang chủ ---------------------------  */
 										
package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import DAO.DAO_HomeTown;
import DAO.DAO_Login;
import DAO.DAO_Room;
import DAO.DAO_Service;
import DAO.DAO_Tenant;
import application.Support;
import application.TitleForms;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AccountLogin;


public class HomePageController implements Initializable{
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
	private Label numberTownLabel; // Hiển thị tổng số tòa nhà đang quản lý
	@FXML
	private Label numberTenantLabel; // Hiển thị tổng số khách thuê phòng
	@FXML
	private Label numberRoomLabel; // Hiển thị tổng số phòng đang quản lý
	@FXML
	private Label numberServiceLabel; // Hiển thị tổng số dịch vụ
	@FXML
	private PieChart RoomPieChart; // Biểu đồ hình tròn
	@FXML
	private Label emptyPercentLabel; // Nơi hiển thị % số phòng trống
	@FXML
	private Label rentedPercentLabel; // Nơi hiển thị % số phòng đã thuê
	@FXML
	private Label namePackServiceLabel; // Nơi hiển thị gói tài khoản
	@FXML
    private FontAwesomeIconView iconPack; // Biểu tượng của loại gói tài khoản 
	@FXML
	private AnchorPane PackAnchorPane; // Form xem gói tài khoản
	@FXML
	private AnchorPane thanksForm; // Form lời cảm ơn
	@FXML
	private Text nameAccountText; // Nơi hiện tên tài khoản tại Form lời cảm ơn
	@FXML
	private Button backButton; // Nút quay lại khi mở Form xem gói tài khoản
	/*----------------------------------------------------------------------------*/
	
	/*---------------------Khai báo hỗ trợ---------------------*/
	private int NumEmptyRoom; // Thuộc tính lưu số phòng trống
	private int NumRentedRoom; // Thuộc tính lưu số phòng đã thuê
	private String namepack; // Thuộc tính phụ để lưu tên của loại dịch vụ
	private Timeline checkDatabaseTimeline; // Timeline kiểm tra sự thay đổi gói tài khoản trên database
	
	/* Timeline kiểm tra sự thay đổi gói tài khoản trên database, trường hợp từ gói kim cương
	 hạ cấp xuống các gói thấp hơn thì tắt Timeline hiển thị hiệu ứng của gói kim cương */
	private Timeline checkLabelTimeline; 
	
	// Thuộc tính để lưu chiều dài, chiều rộng và tọa độ của cửa sổ hiện tại
	private double currentWidth; // Chiều dài hiện tại
	private double currentHeight; // Chiều cao hiện tại
	private double currentX; // Tọa độ x hiện tại
	private double currentY; // Tọa độ y hiện tại
    /*----------------------------------------------------------------------------*/
    
	/*---------------------Phương thức chính của file--------------------*/
	// Phương thức hiển thị biểu tượng của loại gói tài khoản
	public void showNameAndIconPack(Label namepack,String nameicon, Color color) {
		namepack.setTextFill(color); // Đặt màu chữ
		iconPack.setGlyphName(nameicon); // Đặt tên của loại icon
		iconPack.setFill(color); // Đặt màu icon
	}
	// Phương thức hiển thị loại gói tài khoản
	public void showServicePack(String name) {
		// Kiểm tra xem Timeline của gói kim cương có đang chạy không
		if(checkLabelTimeline.getStatus() == Animation.Status.RUNNING) {
			checkLabelTimeline.stop(); // Tắt Timeline
		}
		namePackServiceLabel.setText(name); // Hiển thị gói tài khoản
		if("Gói miễn phí".equals(name)) {
			showNameAndIconPack(namePackServiceLabel,"PUZZLE_PIECE", Color.web("#7F7F7F"));
		}
		else if("Gói cơ bản 1".equals(name)) {
			showNameAndIconPack(namePackServiceLabel, "CHECK", Color.web("#E28E42"));
		}
		else if("Gói cơ bản 2".equals(name)) {
			showNameAndIconPack(namePackServiceLabel, "PLUS_SQUARE", Color.web("#7030A0"));
		}
		else if("Gói cao cấp 1".equals(name)) {
			showNameAndIconPack(namePackServiceLabel, "STREET_VIEW", Color.web("#2E7C82"));
		}
		else if("Gói cao cấp 2".equals(name)) {
			showNameAndIconPack(namePackServiceLabel, "STAR", Color.web("#FF7979"));
		}
		else if("Gói kim cương".equals(name)){
			showNameAndIconPack(namePackServiceLabel, "DIAMOND", Color.web("#2E74B5"));
			namePackServiceLabel.setStyle(
	                     "-fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );" +
	                     "-fx-foreground-color: linear-gradient(from 0% 0% to 100% 200%, "
	                     + "repeat, aqua 0%, lime 25%, yellow 50%, orange 75%, red 100%);"
	                ); // Đặt kiểu CSS cho đối tượng 
			
			// Tạo Timeline để thực hiện hiệu ứng lấp lánh
	        checkLabelTimeline.getKeyFrames().addAll(
	                new KeyFrame(Duration.ZERO, new KeyValue(namePackServiceLabel.opacityProperty(), 1.0)),
	                new KeyFrame(Duration.seconds(0.5), new KeyValue(namePackServiceLabel.opacityProperty(), 0.0)),
	                new KeyFrame(Duration.seconds(1.0), new KeyValue(namePackServiceLabel.opacityProperty(), 1.0))
	        );
	        checkLabelTimeline.setCycleCount(Timeline.INDEFINITE); // Lặp vô hạn
	        checkLabelTimeline.play(); // Chạy timeline
		}
	}
	
	// Phương thức tạo biểu đồ hình tròn PieChart
	public void createRoomPieChart(int NumEmptyRoom, int NumRentedRoom) {
		// Danh sách chứa dữ liệu cho biểu đồ tròn
		ObservableList<PieChart.Data> dataPieChart = FXCollections.observableArrayList();
		// Tạo đối tượng PieChart có tên là "Phòng trống" và giá trị của đối tượng
        PieChart.Data emptyRoomData = new PieChart.Data("Phòng trống", NumEmptyRoom); 
        // Tạo đối tượng PieChart có tên là "Phòng đã thuê" và giá trị của đối tượng
        PieChart.Data rentedRoomData = new PieChart.Data("Phòng đã thuê", NumRentedRoom);
        
        // Thêm đối tượng vào danh sách
	    dataPieChart.add(emptyRoomData);
	    dataPieChart.add(rentedRoomData);
	    
	    RoomPieChart.setData(dataPieChart); // Hiển thị dữ liệu lên biểu đồ tròn
	    
	    /* Kiểm tra xem PieChart đã hiển thị chưa trước khi đặt màu cho PieChart 
	     nếu không có thì getNode() sẽ bị null*/
	    if (RoomPieChart.lookup(".chart-pie") != null) {
	        // Gọi setStyle bên trong Platform.runLater() để đảm bảo chạy trong luồng JavaFX chính
	        Platform.runLater(() -> {
	        	// Đặt màu cho phần biểu đồ "Phòng trống"
	            emptyRoomData.getNode().setStyle("-fx-pie-color: #169e65;"); 
	            // Đặt màu cho phần biểu đồ "Phòng đã thuê"
	            rentedRoomData.getNode().setStyle("-fx-pie-color: #056960;");
	        });
	    }
	}
	
	// Phương thức tính % phòng trống và đã thuê
	public void calculatePercent(int NumEmptyRoom, int NumRentedRoom) {
		// Tính phần trăm trên tổng số phòng
		double PercentEmptyRoom = (NumEmptyRoom / (double)Support.roomList.size()) * 100;
		double PercentRentedRoom = (NumRentedRoom / (double)Support.roomList.size()) * 100;

		// Sử dụng DecimalFormat để chỉ lấy một chữ số sau dấu phẩy
		DecimalFormat df = new DecimalFormat("#.0");

		// Kiểm tra giá trị PercentEmptyRoom trước khi đặt text
		if (PercentEmptyRoom == 0) {
		    emptyPercentLabel.setText("0%");
		} else {
		    emptyPercentLabel.setText(df.format(PercentEmptyRoom) + "%");
		}

		// Kiểm tra giá trị PercentRentedRoom trước khi đặt text
		if (PercentRentedRoom == 0) {
		    rentedPercentLabel.setText("0%");
		} else {
		    rentedPercentLabel.setText(df.format(PercentRentedRoom) + "%");
		}
	}
	
	// Phương thức hiển thị tổng số tòa, số phòng, số khách, số dịch vụ
	public void getInformation() {
		// Hiển thị tổng số tòa nhà
		numberTownLabel.setText(String.valueOf(DAO_HomeTown.getInstance().selectCount(""))); 
					
		// Hiển thị tổng số phòng
		numberTenantLabel.setText(String.valueOf(DAO_Tenant.getInstance().selectCount("")));
					
		// Hiển thị tổng số khách 
		numberRoomLabel.setText(String.valueOf(DAO_Room.getInstance().selectCount(null))); 
					
		// Hiển thị tổng số dịch vụ
		numberServiceLabel.setText(String.valueOf(DAO_Service.getInstance().selectCount("")));
	}
	
	// Phương thức kiểm tra sự thay đổi của Database khi nâng cấp tài khoản
	public void checkForChangesInDatabase() {
		// Truy vấn từ cơ sở dữ liệu để lấy ra thông tin tài khoản đã đăng nhập vào
		AccountLogin x = DAO_Login.getInstance().selectObject(Support.IDAccount, "");
		
		namepack = x.checkRank(x.getRank()); // Lấy thông tin gói tài khoản
		// Nếu có sự thay đổi về gói tài khoản 
		if(!namepack.equals(Support.rankAccount)) {
			Support.rankAccount = namepack; // Đặt lại gói tài khoản mới 
			Support.pointRank = x.getRank(); // Đặt lại cấp bậc tài khoản
			checkDatabaseTimeline.stop(); // Dừng Timeline kiểm tra sự thay đổi trên database
			
			/* Cập nhật thay đổi ngay trên luồng khi chương trình đang chạy, 
			 nếu không có thì phương thức thông báo NoticeAlert sẽ lỗi do
			 có phần showAndWait() */
			Platform.runLater(() -> {
				showServicePack(namepack);
			    Support.NoticeAlert(AlertType.INFORMATION, "CHÚC MỪNG", 
			        "Nâng cấp gói thành công", 
			        "Gói tài khoản của bạn hiện tại: " + namepack);
			});
		}
	}
	
	// Phương thức xem thông tin các gói tài khoản (Nút 'Xem gói')
	@FXML
	public void showPack() {
		thanksForm.setVisible(false);
		PackAnchorPane.setVisible(true);
		backButton.setVisible(true);
	}
	
	// Phương thức quay lại khi đang ở Form xem gói tài khoản
	@FXML
	public void Back() {
		thanksForm.setVisible(true);
		PackAnchorPane.setVisible(false);
		backButton.setVisible(false);
	}
	
	// Phương thức của nút nâng cấp gói 
	@FXML
	public void upgradePack(ActionEvent event) {
		if("Gói kim cương".equals(namepack)) {
			Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, 
					"Gói hiện tại của bạn đã là cao nhất !"
					+ "\nXin cảm ơn đã sử dụng dịch vụ của chúng tôi !");
		} else {
			currentWidth = Support.stage.getWidth(); // Lấy chiều dài hiện tại của cửa sổ
		    currentHeight = Support.stage.getHeight(); // Lấy chiều cao hiện tại của cửa sổ
		    currentX = Support.stage.getX(); // Lấy tọa độ x hiện tại của cửa sổ
		    currentY = Support.stage.getY(); // Lấy tọa độ y hiện tại của cửa sổ
		    
		    /* Đọc và mở file FXML, đồng thời cập nhật thay đổi kích cỡ cửa sổ trên luồng UI Thread 
		       Việc này sẽ đồng bộ kích cỡ cửa sổ ở Form trước sang Form sau */
			Platform.runLater(() -> {
				try {
					// Tải và lấy nội dung từ file FXML từ đường dẫn
					Support.root = FXMLLoader.load(getClass().getResource("/designFXML/upgradePackDesign.fxml"));
					
					// Lấy Scene và Stage từ sự kiện để thực hiện các thao tác cần thiết 
					Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
					Support.scene = new Scene(Support.root); // Tạo đối tượng Scene và lấy root làm nút gốc
					Support.stage.setScene(Support.scene); // Đặt Scene vào Stage
					
					// Đặt tiêu đề cho cửa sổ
					Support.stage.setTitle(TitleForms.TitleServiceForm);
					
					// Lấy ra icon của phần mềm và hiển thị lên cửa sổ
					Support.stage.getIcons().add(Support.icon);
					
					// Lấy kích thước và tọa độ hiện tại của cửa sổ trước đó tham chiếu sang cửa sổ tiếp theo
					Support.stage.setWidth(currentWidth);
		            Support.stage.setHeight(currentHeight);
		            Support.stage.setX(currentX);
		            Support.stage.setY(currentY);
		            
		            // Hiển thị cửa sổ
					Support.stage.show();
				} catch (IOException e) {
					// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
					e.printStackTrace();
				}
		    });
		}
	}
	
    /*---------------------Phương thức mở các Form---------------------*/
	// HomePageController sẽ không có phương thức mở HomePageForm vì chính HomePageForm đang được chạy
	//Mở Form Phòng
	@FXML
	public void OpenRoomForm(ActionEvent event) {
	    currentWidth = Support.stage.getWidth(); // Lấy chiều dài hiện tại của cửa sổ
	    currentHeight = Support.stage.getHeight(); // Lấy chiều cao hiện tại của cửa sổ
	    currentX = Support.stage.getX(); // Lấy tọa độ x hiện tại của cửa sổ
	    currentY = Support.stage.getY(); // Lấy tọa độ y hiện tại của cửa sổ
	    
	    /* Đọc và mở file FXML, đồng thời cập nhật thay đổi kích cỡ cửa sổ trên luồng UI Thread 
	       Việc này sẽ đồng bộ kích cỡ cửa sổ ở Form trước sang Form sau */
	    Platform.runLater(() -> {
	        try {
	        	// Tải và lấy nội dung từ file FXML từ đường dẫn 
	            Support.root = FXMLLoader.load(getClass().getResource("/designFXML/RoomDesign.fxml"));
	            
	            // Lấy Scene và Stage từ sự kiện để thực hiện các thao tác cần thiết 
	            Support.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            Support.scene = new Scene(Support.root); // Tạo đối tượng Scene và lấy root làm nút gốc
	            Support.stage.setScene(Support.scene); // Đặt Scene vào Stage
	            
	            // Đặt tiêu đề cho cửa sổ
	            Support.stage.setTitle(TitleForms.TitleRoomForm);
	            
	            // Lấy ra icon của phần mềm và hiển thị lên cửa sổ
	            Support.stage.getIcons().add(Support.icon);
	            
	            // Lấy kích thước và tọa độ hiện tại của cửa sổ trước đó tham chiếu sang cửa sổ tiếp theo
	            Support.stage.setWidth(currentWidth);
	            Support.stage.setHeight(currentHeight);
	            Support.stage.setX(currentX);
	            Support.stage.setY(currentY);
	            
	            // Hiển thị cửa sổ
	            Support.stage.show();
	        } catch (IOException e) {
	        	// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
	            e.printStackTrace();
	        }
	    });
	}
	
	//Mở Form Khách
	@FXML
	public void OpenTenantForm(ActionEvent event) {
		currentWidth = Support.stage.getWidth(); // Lấy chiều dài hiện tại của cửa sổ
	    currentHeight = Support.stage.getHeight(); // Lấy chiều cao hiện tại của cửa sổ
	    currentX = Support.stage.getX(); // Lấy tọa độ x hiện tại của cửa sổ
	    currentY = Support.stage.getY(); // Lấy tọa độ y hiện tại của cửa sổ
	    
	    /* Đọc và mở file FXML, đồng thời cập nhật thay đổi kích cỡ cửa sổ trên luồng UI Thread 
	       Việc này sẽ đồng bộ kích cỡ cửa sổ ở Form trước sang Form sau */
		Platform.runLater(() -> {
			try {
				// Tải và lấy nội dung từ file FXML từ đường dẫn
				Support.root = FXMLLoader.load(getClass().getResource("/designFXML/TenantDesign.fxml"));
				
				// Lấy Scene và Stage từ sự kiện để thực hiện các thao tác cần thiết 
				Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
				Support.scene = new Scene(Support.root); // Tạo đối tượng Scene và lấy root làm nút gốc
				Support.stage.setScene(Support.scene); // Đặt Scene vào Stage
				
				// Đặt tiêu đề cho cửa sổ
				Support.stage.setTitle(TitleForms.TitleTenantForm);
				
				// Lấy ra icon của phần mềm và hiển thị lên cửa sổ
				Support.stage.getIcons().add(Support.icon);
				
				// Lấy kích thước và tọa độ hiện tại của cửa sổ trước đó tham chiếu sang cửa sổ tiếp theo
				Support.stage.setWidth(currentWidth);
	            Support.stage.setHeight(currentHeight);
	            Support.stage.setX(currentX);
	            Support.stage.setY(currentY);
	            
	            // Hiển thị cửa sổ
				Support.stage.show();
			} catch (IOException e) {
				// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
				e.printStackTrace();
			}
	    });
	}
	
	// Mở Form dịch vụ
	@FXML
	public void OpenServiceForm(ActionEvent event) {
		currentWidth = Support.stage.getWidth(); // Lấy chiều dài hiện tại của cửa sổ
	    currentHeight = Support.stage.getHeight(); // Lấy chiều cao hiện tại của cửa sổ
	    currentX = Support.stage.getX(); // Lấy tọa độ x hiện tại của cửa sổ
	    currentY = Support.stage.getY(); // Lấy tọa độ y hiện tại của cửa sổ
	    
	    /* Đọc và mở file FXML, đồng thời cập nhật thay đổi kích cỡ cửa sổ trên luồng UI Thread 
	       Việc này sẽ đồng bộ kích cỡ cửa sổ ở Form trước sang Form sau */
		Platform.runLater(() -> {
			try {
				// Tải và lấy nội dung từ file FXML từ đường dẫn
				Support.root = FXMLLoader.load(getClass().getResource("/designFXML/ServiceDesign.fxml"));
				
				// Lấy Scene và Stage từ sự kiện để thực hiện các thao tác cần thiết 
				Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
				Support.scene = new Scene(Support.root); // Tạo đối tượng Scene và lấy root làm nút gốc
				Support.stage.setScene(Support.scene); // Đặt Scene vào Stage
				
				// Đặt tiêu đề cho cửa sổ
				Support.stage.setTitle(TitleForms.TitleServiceForm);
				
				// Lấy ra icon của phần mềm và hiển thị lên cửa sổ
				Support.stage.getIcons().add(Support.icon);
				
				// Lấy kích thước và tọa độ hiện tại của cửa sổ trước đó tham chiếu sang cửa sổ tiếp theo
				Support.stage.setWidth(currentWidth);
	            Support.stage.setHeight(currentHeight);
	            Support.stage.setX(currentX);
	            Support.stage.setY(currentY);
	            
	            // Hiển thị cửa sổ
				Support.stage.show();
			} catch (IOException e) {
				// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
				e.printStackTrace();
			}
	    });
	}
	
	// Mở Form hóa đơn
	@FXML
	public void OpenBillForm(ActionEvent event) {
		currentWidth = Support.stage.getWidth(); // Lấy chiều dài hiện tại của cửa sổ
	    currentHeight = Support.stage.getHeight(); // Lấy chiều cao hiện tại của cửa sổ
	    currentX = Support.stage.getX(); // Lấy tọa độ x hiện tại của cửa sổ
	    currentY = Support.stage.getY(); // Lấy tọa độ y hiện tại của cửa sổ
	    
	    /* Đọc và mở file FXML, đồng thời cập nhật thay đổi kích cỡ cửa sổ trên luồng UI Thread 
	       Việc này sẽ đồng bộ kích cỡ cửa sổ ở Form trước sang Form sau */
		Platform.runLater(() -> {
			try {
				// Tải và lấy nội dung từ file FXML từ đường dẫn
				Support.root = FXMLLoader.load(getClass().getResource("/designFXML/BillDesign.fxml"));
				
				// Lấy Scene và Stage từ sự kiện để thực hiện các thao tác cần thiết 
				Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
				Support.scene = new Scene(Support.root); // Tạo đối tượng Scene và lấy root làm nút gốc
				Support.stage.setScene(Support.scene); // Đặt Scene vào Stage
				
				// Đặt tiêu đề cho cửa sổ
				Support.stage.setTitle(TitleForms.TitleBillForm);
				
				// Lấy ra icon của phần mềm và hiển thị lên cửa sổ
				Support.stage.getIcons().add(Support.icon);
				
				// Lấy kích thước và tọa độ hiện tại của cửa sổ trước đó tham chiếu sang cửa sổ tiếp theo
				Support.stage.setWidth(currentWidth);
	            Support.stage.setHeight(currentHeight);
	            Support.stage.setX(currentX);
	            Support.stage.setY(currentY);
	            
	            // Hiển thị cửa sổ
				Support.stage.show();
			} catch (IOException e) {
				// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
				e.printStackTrace();
			}
	    });
	}
	
	// Mở Form thống kê
	@FXML
	public void OpenStatisticsForm(ActionEvent event) {
		currentWidth = Support.stage.getWidth(); // Lấy chiều dài hiện tại của cửa sổ
	    currentHeight = Support.stage.getHeight(); // Lấy chiều cao hiện tại của cửa sổ
	    currentX = Support.stage.getX(); // Lấy tọa độ x hiện tại của cửa sổ
	    currentY = Support.stage.getY(); // Lấy tọa độ y hiện tại của cửa sổ
	    
	    /* Đọc và mở file FXML, đồng thời cập nhật thay đổi kích cỡ cửa sổ trên luồng UI Thread 
	       Việc này sẽ đồng bộ kích cỡ cửa sổ ở Form trước sang Form sau */
		Platform.runLater(() -> {
			try {
				// Tải và lấy nội dung từ file FXML từ đường dẫn
				Support.root = FXMLLoader.load(getClass().getResource("/designFXML/StatisticsDesign.fxml"));
				
				// Lấy Scene và Stage từ sự kiện để thực hiện các thao tác cần thiết 
				Support.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
				Support.scene = new Scene(Support.root); // Tạo đối tượng Scene và lấy root làm nút gốc
				Support.stage.setScene(Support.scene); // Đặt Scene vào Stage
				
				// Đặt tiêu đề cho cửa sổ
				Support.stage.setTitle(TitleForms.TitleStatisticsForm);
				
				// Lấy ra icon của phần mềm và hiển thị lên cửa sổ
				Support.stage.getIcons().add(Support.icon);
				
				// Lấy kích thước và tọa độ hiện tại của cửa sổ trước đó tham chiếu sang cửa sổ tiếp theo
				Support.stage.setWidth(currentWidth);
	            Support.stage.setHeight(currentHeight);
	            Support.stage.setX(currentX);
	            Support.stage.setY(currentY);
	            
	            // Hiển thị cửa sổ
				Support.stage.show();
			} catch (IOException e) {
				// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
				e.printStackTrace();
			}
	    });
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
				stage.getIcons().add(Support.icon); // Hiển thị icon lên cửa sổ
				stage.setTitle(TitleForms.TitleLoginForm); // Đặt tiêu đề cửa sổ
				stage.show(); // Hiển thị cửa sổ
			} catch (IOException e) {
				// Bắt lỗi Input/Output và in ra ngoại lệ (lỗi)
				e.printStackTrace();
			}
		}
	}
	/*-----------------------------------------------------------------------------------------------*/
	
	/*---------------------Phương thức mặc định---------------------*/
	/* Phương thức mặc định được gọi tự động khi một Controller của FXML 
	  được tạo và liên kết với một tập tin FXML
	  + URL arg0: Đối tượng URL đại diện cho đường dẫn của tập tin FXML được liên kết với Controller
	  + ResourceBundle arg1: Đối tượng ResourceBundle cung cấp các thông tin ngôn ngữ 
	  và cấu hình có thể được sử dụng trong ứng dụng 
	  - Tuy nhiên chúng ta sẽ không sử dụng đến 2 tham số này vì nó là phương thức của 
	  lớp cha Initializable*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		HomeButton.setStyle(Support.colorButton); // Chỉnh màu nút Trang chủ khi đang chạy Form
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
		nameAccountText.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập tại Form cảm ơn
		
		// Lấy danh sách tất cả tòa nhà
		Support.homeList = FXCollections.observableArrayList();
		Support.homeList.addAll(DAO_HomeTown.getInstance().selectALL());
				
		// Lấy danh sách tất cả các phòng
		Support.roomList = FXCollections.observableArrayList();
		Support.roomList.addAll(DAO_Room.getInstance().selectALL());
		
		// Gọi phương thức lấy thông tin
		getInformation();
		
		// Lấy dữ liệu về phòng trống và phòng đã thuê từ cơ sở dữ liệu
		NumEmptyRoom = DAO_Room.getInstance().selectCount("0");
	    NumRentedRoom = DAO_Room.getInstance().selectCount("1");
	    
		createRoomPieChart(NumEmptyRoom, NumRentedRoom); // Tạo biểu đồ hình tròn Pie-Chart
		calculatePercent(NumEmptyRoom, NumRentedRoom); // Gọi phương thức tính % số phòng trống và đã thuê
		
		checkLabelTimeline = new Timeline();
		namepack = Support.rankAccount; // Lấy thông tin gói tài khoản đang sử dụng
		showServicePack(namepack); // Gọi phương thức hiển thị gói tài khoản
		
		/* Khởi tạo Timeline với chu kỳ 10 giây để kiểm tra sự thay đổi trên database khi nâng cấp gói
		 tài khoản thành công */
		checkDatabaseTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), event -> checkForChangesInDatabase())
        );
		checkDatabaseTimeline.setCycleCount(Timeline.INDEFINITE); // Thiết lập chu kỳ Timeline
		checkDatabaseTimeline.play(); // Chạy Timeline
	}
}
