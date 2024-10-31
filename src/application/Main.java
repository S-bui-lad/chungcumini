package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {	
			// Tải và lấy nội dung từ file FXML từ đường dẫn
			Parent root = FXMLLoader.load(getClass().getResource("/designFXML/LoginDesign.fxml"));
			Scene scene = new Scene(root); // Tạo đối tượng Scene
			primaryStage.setResizable(false);
			primaryStage.setScene(scene); // Đặt Scene vào Stage
			Support.icon = new Image("/imageIcon/icon_miniapartment.png");
			primaryStage.getIcons().add(Support.icon);
			primaryStage.setTitle("Phần Mềm Quản Lý Chung Cư Mini - Đăng nhập");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
