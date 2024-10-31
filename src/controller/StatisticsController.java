package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import DAO.DAO_Bill;
import application.Support;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StatisticsController implements Initializable{
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
	private LineChart<String , Number> monthlyRevenueLineChart;
	@FXML
	private BarChart<String, Number> yearlyRevenueBarChart;
	private XYChart.Series<String, Number> monthChart;
	private XYChart.Series<String,Number> yearChart;
	/*----------------------------------------------------------------------------*/
	public int getYear() {
		LocalDate year = LocalDate.now();
		return year.getYear();
	}
	@SuppressWarnings("unchecked")
	public void setTurnoverMonthToLineChart() {
		monthChart = new XYChart.Series<>();
		XYChart.Data<String, Number> jan = new XYChart.Data<>("jan",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(1, getYear())));
		XYChart.Data<String, Number> feb = new XYChart.Data<>("feb",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(2, getYear())));
		XYChart.Data<String, Number> mar = new XYChart.Data<>("mar",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(3, getYear())));
		XYChart.Data<String, Number> apr = new XYChart.Data<>("apr",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(4, getYear())));
		XYChart.Data<String, Number> may = new XYChart.Data<>("may",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(5, getYear())));
		XYChart.Data<String, Number> jun = new XYChart.Data<>("jun",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(6, getYear())));
		XYChart.Data<String, Number> jul = new XYChart.Data<>("jul",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(7, getYear())));
		XYChart.Data<String, Number> aug = new XYChart.Data<>("aug",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(8, getYear())));
		XYChart.Data<String, Number> sep = new XYChart.Data<>("sep",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(8, getYear())));
		XYChart.Data<String, Number> oct = new XYChart.Data<>("oct",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(10, getYear())));
		XYChart.Data<String, Number> nov = new XYChart.Data<>("nov",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(11, getYear())));
		XYChart.Data<String, Number> dec = new XYChart.Data<>("dec",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(12, getYear())));
		monthChart.getData().addAll(jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec);
		monthlyRevenueLineChart.getData().add(monthChart);
		
		/* Kiểm tra xem LineChart đã hiển thị chưa trước khi đặt màu cho PieChart 
	     nếu không có thì getNode() sẽ bị null*/
	    if (monthlyRevenueLineChart.lookup(".chart-series-line") != null) {
	        // Gọi setStyle bên trong Platform.runLater() để đảm bảo chạy trong luồng JavaFX chính
	        Platform.runLater(() -> {
	        	// Áp dụng kiểu CSS để thay đổi màu sắc của đường
	        	monthlyRevenueLineChart.lookup(".chart-series-line").setStyle("-fx-stroke: #056960;"); 
	        });
	    }
	}
	
	@SuppressWarnings("unchecked")
	public void setTurnoverYearToBarChart() {
		yearChart = new XYChart.Series<>();
		yearChart.getData().add(new XYChart.Data<String, Number>("2024",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear()))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2025",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 1))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2026",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 2))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2027",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 3))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2028",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 4))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2029",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 5))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2030",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 6))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2031",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 7))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2032",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 8))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2033",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 9))));
		yearChart.getData().add(new XYChart.Data<String, Number>("2034",Double.parseDouble(DAO_Bill.getInstance().selectTurnover(-1, getYear() + 10))));
		
		// Thêm màu cho cột trong BarChart
	    for (XYChart.Data<String, Number> data : yearChart.getData()) {
	        javafx.scene.Node node = data.getNode();
	        if (node != null) {
	          Platform.runLater(() -> {
		           node.setStyle("-fx-bar-fill: #056960;");
	          });
	        }
	    }
	    yearlyRevenueBarChart.getData().addAll(yearChart);
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
	public void OpenBillForm(ActionEvent event) {
		try {
			Support.root = FXMLLoader.load(getClass().getResource("/designFXML/BillDesign.fxml"));
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
		setTurnoverYearToBarChart();
		setTurnoverMonthToLineChart();
		StatisticsButton.setStyle(Support.colorButton); // Chỉnh màu nút Trang chủ khi đang chạy Form
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
	}

}
