
/*  --------------------------- Controller của Form phòng ---------------------------  */

package controller;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import DAO.DAO_HomeTown;
import DAO.DAO_Room;
import DAO.DAO_Service;
import application.Support;
import application.TitleForms;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.HomeTown;
import model.Room;
import model.Service;

public class RoomController implements Initializable{
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
	// 1. Phần tòa nhà:
	// 1.1. Phần nhập thông tin tòa nhà
	@FXML
	private TextField HomeTownIDTextField; // Nơi nhập mã tòa nhà
	@FXML
	private TextField HomeTownAddressTextField; // Nơi nhập địa chỉ tòa nhà;
	// 1.2. Bảng hiển thị thông tin các tòa nhà
	@FXML
	private TableView<HomeTown> HomeTownTableView; // Bảng hiển thị thông tin các tòa nhà
	@FXML
	private TableColumn<HomeTown, Integer> ordinalHomeTown; // Cột số thứ tự tòa nhà
	@FXML
	private TableColumn<HomeTown, String> HomeTownID_col; // Cột mã tòa
	@FXML
	private TableColumn<HomeTown, String> HomeTownAddress_col; // Cột địa chỉ
	//-----------------
	// 2. Phần phòng
	@FXML
	private Tab RoomTab; // Tab phòng 
	
	// 2.1. Phần tìm kiếm và lọc phòng
	@FXML
	private ComboBox<String> chooseTownComboBox; // Hộp danh sách lựa chọn thông tin đầy đủ của tòa nhà
	@FXML
	private ComboBox<String> chooseStateComboBox; // Hộp danh sách lựa chọn trạng thái phòng
	
	// 2.2. Phần nhập thông tin phòng
	@FXML
	private ComboBox<String> chooseTownComboBox2; // Lựa chọn mã tòa nhà để thêm thông tin
	@FXML
    private TextField searchRoomTextField; // Nơi nhập thông tin về phòng cần tìm kiếm
	@FXML
	private TextField RoomIDTextField; // Nơi nhập mã phòng
	@FXML
	private TextField RoomTypeTextField; // Nơi nhập loại phòng
	@FXML
	private TextField RoomPriceTextField; // Nơi nhập giá phòng
	
	// 2.3. Bảng phòng
	@FXML
	private TableView<Room> RoomTableView; // Bảng hiển thị thông tin tất cả các phòng của mọi tòa
	@FXML
	private TableColumn<Room, Integer> ordinalRoom; // Cột số thứ tự của phòng
	@FXML
	private TableColumn<Room, String> RoomID_col; // Cột mã phòng
	@FXML
	private TableColumn<Room, String> TypeRoom_col; // Cột loại phòng
	@FXML
	private TableColumn<Room, String> PriceRoom_col; // Cột giá phòng
	@FXML
	private TableColumn<Room, String> StateRoom; // Cột trạng thái phòng
	/*-----------------------------------------------------------------------------------------------*/
	
	/*---------------------Khai báo hỗ trợ---------------------*/
	// Danh sách sử dụng cho việc kiểm tra tòa nhà có tồn tại phòng hay không
	private ObservableList<Room> checkListRoom; 
	
	// Danh sách phòng lấy ra từ bộ lọc hoặc được tìm kiếm
	private ObservableList<Room> listRoomState; 
	
	// Danh sách sử dụng cho việc kiểm tra tòa nhà có tồn tại dịch vụ hay không
	private ObservableList<Service> checkListService;
	private int rankAccount; // Thuộc tính hỗ trợ kiểm tra quyền của tài khoản
	/*----------------------------------------------------------------------------*/
	
	/*---------------------Phương thức chính của file--------------------*/
	/*---------------------------Phần tòa nhà---------------------------*/
	// Phương thức kiểm tra gói tài khoản đăng kí
	public boolean checkRank(ObservableList<Room> list) {
		if(rankAccount == 0 && list.size() == 5) {
			return false;
		}
		else if(rankAccount == 1 && list.size() == 10) {
			return false;
		}
		else if(rankAccount == 2 && list.size() == 20) {
			return false;
		}
		else if(rankAccount == 3 && list.size() == 50) {
			return false;
		}
		else if(rankAccount == 4 && list.size() == 100) {
			return false;
		}
		return true;
	}
	
	// Phương thức kiểm tra danh sách tòa nhà tổng quát để mở Tab phòng
	public void validRoomTab(ObservableList<HomeTown> list) {
		// Nếu danh sách tòa trống, ta sẽ không thể chỉnh sửa lại Tab phòng
		if(list.size() > 0) {
			RoomTab.setDisable(false);
		}
		else {
			RoomTab.setDisable(true);
		}
	}
	//Phương thức kiểm tra phần nhập thông tin tòa nhà xem có trống phần nào không
	public boolean checkHomeTownEmpty() {
		// Kiểm tra nội dung trên phần nhập của mã tòa và địa chỉ tòa
		if(HomeTownIDTextField.getText().isEmpty() || HomeTownAddressTextField.getText().isEmpty()) {
			// Nếu trống thì trả về True
			return true;
		}
		return false;
	}
	
	// Phương thức thiết lập thông tin từ danh sách tòa nhà lên bảng hiển thị
	public void setHomeTownToTable(ObservableList<HomeTown> list) {
		// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
		ordinalHomeTown.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(list.indexOf(cellData.getValue()) + 1).asObject());
		
		// Thiết lập hiển thị thông tin mã tòa được lấy từ thuộc tính 'townID' lên cột mã tòa
		HomeTownID_col.setCellValueFactory(new PropertyValueFactory<HomeTown, String>("townID"));
		
		// Thiết lập hiển thị thông tin địa chỉ tòa nhà được lấy từ thuộc tính 'address' lên cột địa chỉ
		HomeTownAddress_col.setCellValueFactory(new PropertyValueFactory<HomeTown, String>("address"));
		
		// Đặt dữ liệu từ danh sách các đối tượng tòa nhà lên bảng hiển thị tòa nhà
		HomeTownTableView.setItems(list);
	}
	
	// Phương thức thêm tòa nhà (nút ấn 'Thêm')
	@FXML
	public void addHomeTown() {
		try {
			// Kiểm tra nội dung nhập thông tin của mục tòa
			if(checkHomeTownEmpty()) {
				// Gọi phương thức thông báp từ lớp Support
				Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Vui lòng điền đầy đủ thông tin!");
			}
			else {
				boolean duplicatedHomeTownID = false; // Biến kiểm tra trùng mã tòa
				for(HomeTown temp : Support.homeList) {
					// Kiểm tra xem mã tòa nhập vào đã tồn tại trong danh sách các tòa
					if(temp.getTownID().equalsIgnoreCase(HomeTownIDTextField.getText())) {
						duplicatedHomeTownID = true;
						break;
					}
				}
				// Nếu không có mã tòa nào bị trùng
				if(!duplicatedHomeTownID) {
					// Khởi tạo đối tượng mới
					HomeTown x = new HomeTown(HomeTownIDTextField.getText(), HomeTownAddressTextField.getText());
					
					// Truy vấn thêm thông tin vào cơ sở dữ liệu thông qua lớp DAO_HomeTown
					DAO_HomeTown.getInstance().insertData(x, "", "");
					Support.homeList.add(x); // Thêm đối tượng vào danh sách
					validRoomTab(Support.homeList); // Gọi phương thức kiểm tra để mở Tab phòng
					// Thêm thông tin tòa nhà mới và hộp các bảng chọn
					chooseTownComboBox.getItems().add(x.toString());
					chooseTownComboBox2.getItems().add(x.getTownID());
					
					// Thông báo thêm thành công
					Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Thêm thông tin thành công !");
				}
				else {
					// Thông báo trùng mã tòa
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Mã tòa bị trùng !"
							+ "\nMã tòa không phân biệt chữ hoa và chữ thường"
							+ "\nVui lòng nhập mã tòa khác !");
				}
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// Phương thức lấy thông tin tòa nhà trên bảng và hiển thị lên khu vực nhập thông tin
	@FXML
	public void viewHomeTown() {
		// Lấy chỉ số của dòng đang được chọn trên bảng hiển thị tòa nhà
		int infoData = HomeTownTableView.getSelectionModel().getSelectedIndex();
		
		// Nếu không có dòng nào được chọn
	   	if(infoData <= -1) {
	   		return;
	   	}
	   	// Truyền dữ liệu từ dòng được chọn vào phần nhập thông tin
	   	HomeTownIDTextField.setText(HomeTownID_col.getCellData(infoData));
	   	HomeTownAddressTextField.setText(HomeTownAddress_col.getCellData(infoData));
	}
	
	// Phương thức cập nhật tòa nhà (Nút ấn 'Cập nhật')
	@FXML
	public void updateHomeTown() {
		// Kiểm tra nội dung sửa thông tin của mục tòa
		if(checkHomeTownEmpty()) {
			// Gọi phương thức thông báp từ lớp Support
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI CẬP NHẬT THÔNG TIN", 
					"Vui lòng điền đầy đủ thông tin!");
		}else {
			try {
				// Biến so sánh phần nhập mã tòa và mã tòa được lấy từ bảng
				boolean compareHomeTownID = false; 
				for(HomeTown temp : Support.homeList) {
					if(temp.getTownID().equalsIgnoreCase(HomeTownIDTextField.getText())) {
						// Nếu phần sửa tòa nhà không có thay đổi
						compareHomeTownID = true;
						break;
					}
				}
				// Nếu mà tòa ở phần nhập trùng với mã tòa được lấy ra từ phần cần chỉnh sửa
				if(compareHomeTownID) {
					// Khởi tạo 1 đối tượng mới
					HomeTown x = new HomeTown(HomeTownIDTextField.getText(), HomeTownAddressTextField.getText());
					
					// Truy vấn cập nhật thông tin vào cơ sở dữ liệu thông qua lớp DAO_HomeTown
					DAO_HomeTown.getInstance().updateData(x, "", "");
					
					// Xóa hết danh sách các tòa hiện tại do có sự thay đổi thông tin dữ liệu
					Support.homeList.clear();
					// Truy vấn lấy tất cả thông tin tòa nhà từ cơ sở dữ liệu thông qua lớp DAO_HomeTown
					Support.homeList.addAll(DAO_HomeTown.getInstance().selectALL());
					
					// Đặt lại dữ liệu từ danh sách tòa lên bảng hiển thị tòa nhà
					HomeTownTableView.setItems(Support.homeList);
					
					// Hiển thị thông báo sửa thông tin thành công
					Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Sửa thông tin thành công !");
				}else {
					// Hiển thị thông báo sửa thông tin không thành công
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI SỬA THÔNG TIN", "Không thể sửa mã tòa nhà!");
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	// Phương thức xóa nội dung trên phần nhập thông tin tòa nhà (Nút ấn 'Tẩy')
	@FXML
	public void eraseHomeTownInTextField() {
		// Đặt lại nội dung trên phần nhập mã tòa
		HomeTownIDTextField.setText("");
		// Đặt lại nội dung trên phần nhập địa chỉ
		HomeTownAddressTextField.setText("");
	}
	// Phương thức xóa tòa nhà (Nút ấn 'Xóa')
	@FXML
	public void deleteHomeTown() {
		/* Khi nút xóa được ấn, nó sẽ kiểm tra xem tại tòa đó đã có phòng và dịch vụ hay chưa, 
		nếu chưa thì sẽ xóa được tòa, nếu đã có thì sẽ không thể xóa tòa nhà. Việc này thực hiện
		bằng cách kiểm tra xem danh sách phòng, dịch vụ được lấy ra từ mã phòng cần xóa xem có 
		rỗng hay không. Nếu có thì sẽ xóa dữ liệu cả 2 danh sách. Nếu không xóa, mỗi lần ấn nút xóa mà 
		cả 2 danh sách kiểm tra không rỗng, nó sẽ tự thêm dữ liệu vào thông qua truy vấn (*) và (**), 
		điều này khiến danh sách luôn trong tình trạng có dữ liệu. Kết quả dẫn đến việc, khi ta 
		đã xóa phòng ở tòa cần xóa, nhưng danh sách kiểm tra phòng luôn có dữ liệu, 
		chương trình luôn bắt lỗi tòa có chứa phòng và dịch vụ khiến ta không thể xóa tòa */
		if(checkListRoom.size() > 0 ) {
			checkListRoom.clear();
		}
		else if(checkListService.size() > 0) {
			checkListService.clear();
		}
		// Lấy đối tượng đang được chọn trên bảng tòa nhà
	    HomeTown selection = HomeTownTableView.getSelectionModel().getSelectedItem();
	    
	    // Kiểm tra lựa chọn, nếu rỗng thì thông báo
	    if(selection == null) {
	    	Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", "LỖI XÓA THÔNG TIN", "Không thể xóa tòa nhà !"
	    			+ "\nVui lòng chọn tòa nhà cần xóa trên bảng ");
	    }
	    else {
	    	// (*) Lấy thông tin phòng theo mã tòa từ cơ sở dữ liệu thông qua lớp DAO_Room
	    	checkListRoom.addAll(DAO_Room.getInstance().selectByCondition(selection.getTownID(), null, null));
	    	
	    	// (**) Lấy thông tin dịch vụ theo mã tòa từ cơ sở dữ liệu thông qua lớp DAO_Service
	    	checkListService.addAll(DAO_Service.getInstance().selectByCondition(selection.getTownID(), null, null));
	    	
	    	// Nếu như danh sách tồn tại dữ liệu, điều đó chứng minh tòa nhà đang có phòng
		    if(checkListRoom.size() > 0) {
		    	// Thông báo lỗi
		    	Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", "LỖI XÓA THÔNG TIN", 
		    			"Không thể xóa tòa nhà hiện đang có phòng !"
		    			+ "\nHãy xóa phòng của tòa cần xóa trước");
		    }
		    // Nếu như danh sách dịch vụ có dữ liệu
		    else if(checkListService.size() > 0) {
		    	// Thông báo lỗi
		    	Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", "LỖI XÓA THÔNG TIN", 
		    			"Không thể xóa tòa nhà hiện đang có dịch vụ !"
		    			+ "\nHãy xóa dịch vụ của tòa cần xóa trước");
		    }
		    else {
		    	// Xóa thông tin tòa nhà đang được chọn trên bảng trên cơ sở dữ liệu
		    	DAO_HomeTown.getInstance().deleteData(selection, "");
		    	
		    	// Xóa thông tin tin tòa nhà đang được chọn trong danh sách các tòa nhà
				Support.homeList.remove(selection);
				
				// Xóa thông tin tin tòa nhà khỏi hộp lựa chọn tòa nhà
				chooseTownComboBox.getItems().remove(selection.toString());
				chooseTownComboBox2.getItems().remove(selection.getTownID());
				
				// Đặt lại dữ liệu từ danh sách tòa nhà lên bảng hiển thị tòa nhà
				HomeTownTableView.setItems(Support.homeList);
				validRoomTab(Support.homeList); // Gọi phương thức kiểm tra để mở Tab phòng
				// Thông báo xóa tòa nhà thành công
				Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Xóa thông tin thành công !");
		    }
	    }
	    
	}
	/*-------------------------------------------------------------------------------------------*/
	
	/*---------------------------Phần phòng---------------------------*/
	//Phương thức kiểm tra nhập thông tin phòng xem có trống phần nào không
	public boolean checkRoomEmpty() {
		if(chooseTownComboBox2.getSelectionModel().isEmpty() 
				||RoomIDTextField.getText().isEmpty() 
				|| RoomTypeTextField.getText().isEmpty()
				|| RoomPriceTextField.getText().isEmpty()) {
			return true;
		}
		return false;
	}
	// Phương thức kiểm tra chuỗi nhập giá tiền dịch vụ hợp lệ
	public boolean isValidPrice(String price) {
			/*Sử dụng biểu thức chính quy để kiểm tra chuỗi
				\\d*: Kiểm tra số trước dấu . có 0 hoặc nhiều chữ số.
				\\.?: Kiểm tra xem có thể có dấu . hoặc không có
		 		0*$:  Kiểm tra xem số sau dấu . phải là số 0 hoặc nhiều số 0
		 		\\d+: Kiểm tra xem có phải số tự nhiên không âm */
		if(price.matches("\\d*\\.?0*$")) {
			double temp1 = Double.parseDouble(price); // Chuyển số thực về chuỗi
			if(temp1 >= 0) return true;
		}
		else if(price.matches("\\d+")) {
			int temp = Integer.parseInt(price); // Chuyển số tự nhiên không âm về chuỗi
			if(temp >= 0) return true;
		}
	       	return false;
	}
		
	// Phương thức chuyển định dạng giá tiền
	private String formatPrice(String price) {
	    // Chuyển chuỗi thành số nguyên
	    double temp = Double.parseDouble(price);

	    // Tạo đối tượng NumberFormat để định dạng số theo định dạng tiền tệ của Hoa Kỳ.
	    NumberFormat formatNum = NumberFormat.getCurrencyInstance(Locale.US);

	    // Định dạng số theo chuẩn tiền tệ Hoa Kì
	    String formattedNum = formatNum.format(temp);

	    // Trả về chuỗi đã định dạng, đã xóa kí tự dollar $
	    return formattedNum.replace("$", "");
	}
		
	/*Phương thức chuyển ngược từ định dạng tiền về dạng số bình thường 
	  khi ấn chọn dịch vụ trên bảng, số tiền từ định dạng tiền sẽ về số bình thường */
	public String parsePrice(String price) {
		String temp = price.replace(",", ""); // Xóa dấu ',' của chuỗi
		int position = temp.indexOf("."); // Tìm vị trí dấu '.' trong chuỗi
		return temp.substring(0, position); // Lấy chuỗi từ đầu chuỗi đến trước vị trí dấu '.'
	}
	
	// Phương thức thiết lập thông tin từ danh sách phòng lên bảng hiển thị
	public void setRoomToTable(ObservableList<Room> list) {
		// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
		ordinalRoom.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(list.indexOf(cellData.getValue()) + 1).asObject());
		
		// Thiết lập hiển thị thông tin mã phòng được lấy từ thuộc tính 'roomID' lên cột mã phòng
		RoomID_col.setCellValueFactory(new PropertyValueFactory<Room, String>("roomID"));
		
		// Thiết lập hiển thị thông tin loại phòng được lấy từ thuộc tính 'typeroom' lên cột loại phòng
		TypeRoom_col.setCellValueFactory(new PropertyValueFactory<Room, String>("typeroom"));
		
		// Thiết lập hiển thị thông tin trạng thái phòng được lấy từ thuộc tính 'stateroom' lên cột trạng thái
		StateRoom.setCellValueFactory(new PropertyValueFactory<Room, String>("stateroom"));
		
		// Thiết lập hiển thị giá dịch vụ chuẩn tiền tệ thông qua hàm định dạng lên cột giá phòng 
		PriceRoom_col.setCellValueFactory(cellData ->
        new SimpleStringProperty(formatPrice(cellData.getValue().getPriceroom())));
		
		// Đặt lại dữ liệu lên bảng hiển thị phòng
		RoomTableView.setItems(list);
	}
	
	// Phương thức tìm phòng theo phần tìm kiếm (Nút ấn có hình kính lúp)
	@FXML
	public void searchRoom() {
		String text = searchRoomTextField.getText().trim();
		if(text.isEmpty()) {
			return;
		}
		listRoomState.clear(); // Xóa danh sách phòng được lấy theo điều kiện
		// Nếu lựa chọn từ hộp tòa nhà và hộp trạng thái phòng đều rỗng
		if(chooseTownComboBox.getValue() == null 
				&& chooseStateComboBox.getValue() == null) {
			// Truy vấn tìm kiếm trong cơ sở dữ liệu
			listRoomState.addAll(DAO_Room.getInstance().searching(text, "", ""));
		} 
		// Nếu lựa chọn từ hộp tòa nhà là rỗng và hộp trạng thái phòng khác rỗng
		else if(chooseTownComboBox.getValue() == null 
				&& chooseStateComboBox.getValue() != null) {
			// Kiểm tra nội dung lựa chọn và truy vấn theo lựa chọn đó
			if("Trống".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().searching(
						text, "", "0"));
			}
			else if("Đã thuê".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().searching(
						text, "", "1"));
			}
		}
		else if(chooseTownComboBox.getValue() != null 
				&& chooseStateComboBox.getValue() == null) {
			String town = chooseTownComboBox.getValue(); //Lấy lựa chọn tòa nhà từ hộp tòa nhà
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu '-' có trong chuỗi
			// Truy vấn tìm kiếm trong cơ sở dữ liệu qua lớp DAO_Room
			listRoomState.addAll(DAO_Room.getInstance().searching
					(text, codeHomeTownID[0], ""));
		}
		else {
			String town = chooseTownComboBox.getValue(); //Lấy lựa chọn tòa nhà từ hộp tòa nhà
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu '-' có trong chuỗi
			// Kiểm tra nội dung lựa chọn và truy vấn theo lựa chọn đó
			if("Trống".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().searching
						(text, codeHomeTownID[0], "0"));
			}
			else if("Đã thuê".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().searching
						(text, codeHomeTownID[0], "1"));
			}
		}
		// Đặt lại nội dung hiển thị từ danh sách tìm kiếm theo điều kiện lên bảng hiển thị
		setRoomToTable(listRoomState);
	}
	
	// Phương thức làm mới lại nội dung trên bảng (Nút ấn có hình vòng tròn 2 mũi tên )
	@FXML
	public void refreshRoom() {
		listRoomState.clear(); // Xóa dữ liệu từ danh sách phòng được tìm kiếm/lọc theo điều kiện
		chooseTownComboBox.setValue(null); // Đặt lại lựa chọn trên hộp chọn tòa nhà là rỗng
		chooseStateComboBox.setValue(null); // Đặt lại lựa chọn trên hộp chọn trạng thái là rỗng
		// Đặt lại nội dung hiển thị từ danh sách phòng tổng quát lên bảng hiển thị
		setRoomToTable(Support.roomList);
	}
	
	// Phương thức lọc phòng theo lựa chọn từ hộp lựa chọn (Nút ấn "Lọc")
	@FXML
	public void filterMenuRoom() {
		// Nếu lựa chọn từ hộp trạng thái là rỗng và hộp tòa nhà khác rỗng
		if(chooseStateComboBox.getValue() == null && 
				chooseTownComboBox.getValue() != null) {
			String town = chooseTownComboBox.getValue(); ///Lấy lựa chọn tòa nhà từ hộp tòa nhà
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu - có trong chuỗi
			listRoomState.clear(); // Xóa dữ liệu trong danh sách phòng có điều kiện
			
			// Truy vấn danh sách phòng có điều kiện trong cơ sở dữ liệu qua lớp DAO_Room
			listRoomState.addAll(DAO_Room.getInstance().selectByCondition(codeHomeTownID[0], null, null));
			// Đặt lại nội dung hiển thị từ danh sách phòng tổng quát lên bảng hiển thị
			setRoomToTable(listRoomState);
		}
		// Nếu lựa chọn từ hộp tòa nhà và hộp trạng thái phòng đều rỗng
		else if(chooseStateComboBox.getValue() == null && 
				chooseTownComboBox.getValue() == null) {
			// Thông báo lỗi
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", null, "Vui lòng chọn 1 trong 2 mục !");
		}
		// Nếu lựa chọn từ hộp trạng thái khác rỗng và hộp tòa nhà là rỗng
		else if(chooseStateComboBox.getValue() != null && 
				chooseTownComboBox.getValue() == null) {
			listRoomState.clear(); // Xóa dữ liệu trong danh sách phòng có điều kiện
			
			// Kiểm tra nội dung lựa chọn và truy vấn theo lựa chọn đó
			if("Trống".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(null, "0", null));
			}
			else if("Đã thuê".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(null, "1", null));
			}
			// Đặt lại nội dung hiển thị từ danh sách lọc theo điều kiện lên bảng hiển thị
			setRoomToTable(listRoomState);
		}
		// Nếu lựa chọn từ hộp tòa nhà và hộp trạng thái phòng khác rỗng
		else if(chooseStateComboBox.getValue() != null && 
				chooseTownComboBox.getValue() != null) {
			String town = chooseTownComboBox.getValue(); ///Lấy lựa chọn tòa nhà từ hộp tòa nhà
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu - có trong chuỗi
			listRoomState.clear(); // Xóa dữ liệu trong danh sách phòng có điều kiện
			if("Trống".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(codeHomeTownID[0], "0", null));
			}
			else if("Đã thuê".equals(chooseStateComboBox.getValue())) {
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(codeHomeTownID[0], "1", null));
			}
			// Đặt lại nội dung hiển thị từ danh sách lọc theo điều kiện lên bảng hiển thị
			setRoomToTable(listRoomState);
		}
	}
	
	// Phương thức thêm phòng (Nút ấn 'Thêm')
	@FXML
	public void addRoom() {
		try {
			// Kiểm tra nội dung ở phần nhập xem có trống không
			if(checkRoomEmpty()) {
				Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Vui lòng điền đầy đủ thông tin!");
			}
			// Kiểm tra giá phòng nhập vào có hợp lệ hay không
			else if(!isValidPrice(RoomPriceTextField.getText())) {
				Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Vui lòng điền mệnh giá tiền hợp lệ !");
			}
			else {
				boolean duplicatedRoomID = false; // Biến kiểm tra trùng mã phòng
				// Nếu danh sách phòng tổng quát có chứa dữ liệu phòng thì mới kiểm tra trùng mã
				if(Support.roomList.size() > 0) {
					listRoomState.clear();
					listRoomState.addAll(
							DAO_Room.getInstance().selectByCondition(chooseTownComboBox2.getValue(), null, null));
					if(listRoomState.size() > 0) {
						for(Room x : listRoomState) {
							// So sánh mã phòng trong danh sách xem có trùng với phần nhập phòng hay không
							if(x.getRoomID().equalsIgnoreCase(RoomIDTextField.getText())
									) {
								duplicatedRoomID = true;
								break;
							}
						}
					}
				}
				// Nếu không phát hiện trùng mã phòng
				if(!duplicatedRoomID) {
					if(!checkRank(Support.roomList)) {
						Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", "Số phòng đạt giới hạn", 
								"Vui lòng nâng cấp tài khoản để tiếp tục sử dụng chức năng này !");
					}
					else {
						// Khởi tạo đối tượng phòng mới, mặc định phòng đó có trạng thái là trống
						Room x = new Room(RoomIDTextField.getText(), RoomTypeTextField.getText(),
								"Trống", RoomPriceTextField.getText());
						
						// Truy vấn thêm thông tin phòng vào cơ sở dữ liệu qua lớp DAO_Room
						DAO_Room.getInstance().insertData(x, chooseTownComboBox2.getValue(), "");
						Support.roomList.add(x); // Thêm phòng mới vào danh sách phòng tổng quát
						
						// Thông báo thêm phòng thành công
						Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Thêm thông tin thành công !");
					}
				}
				else {
					// Thông báo lỗi thêm phòng do trùng mã phòng
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI THÊM THÔNG TIN", "Mã phòng bị trùng !"
							+ "\nMã phòng không phân biệt chữ hoa và chữ thường"
							+ "\nVui lòng nhập mã phòng khác !");
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// Phương thức lấy thông tin phòng trên bảng và hiển thị lên khu vực nhập thông tin
	@FXML
	public void viewRoom() {
		// Lấy chỉ số của dòng đang được chọn trên bảng hiển thị phòng
		int infoData = RoomTableView.getSelectionModel().getSelectedIndex();
		// Nếu không có dòng nào được chọn
	   	if(infoData <= -1) {
	   		return;
	   	}
	   	// Đặt thông tin vào phần nhập tương ứng
	   	RoomIDTextField.setText(RoomID_col.getCellData(infoData));
	   	RoomTypeTextField.setText(TypeRoom_col.getCellData(infoData));
	   	RoomPriceTextField.setText(parsePrice(PriceRoom_col.getCellData(infoData)));
	}
	
	// Phương thức cập nhật phòng (Nút ấn 'Cập nhật')
	@FXML
	public void updateRoom() {
		// Kiểm tra nội dung nhập xem có trống không
		if(checkRoomEmpty()) {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI CẬP NHẬT THÔNG TIN", "Vui lòng chọn thông tin cần sửa trên bảng !");
		}
		// Kiểm tra giá phòng hợp lệ
		else if(!isValidPrice(RoomPriceTextField.getText())) {
			Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI CẬP NHẬT THÔNG TIN", "Vui lòng điền mệnh giá tiền hợp lệ !");
		}
		else {
			try {
				// Biến so sánh phần nhập mã tòa và mã tòa được lấy từ bảng
				boolean compareRoomID = false; 
				// Nếu danh sách phòng không rỗng thì mới so sánh
				if(Support.roomList.size() > 0) {
					for(Room temp : Support.roomList) {
						if(temp.getRoomID().equals(RoomIDTextField.getText())) {
							compareRoomID = true;
							break;
						}
					}
					// Nếu mà tòa ở phần nhập trùng với mã tòa được lấy ra từ phần cần chỉnh sửa
					if(compareRoomID) {
						// Tạo mới đối tượng phòng
						Room x = new Room(RoomIDTextField.getText(), RoomTypeTextField.getText(),
								"Trống", RoomPriceTextField.getText());
						
						// Truy vấn cập nhật thông tin phòng vào cơ sở dữ liệu qua lớp DAO_Room
						DAO_Room.getInstance().updateData(x, chooseTownComboBox2.getValue(), "");
						
						// Xóa danh sách phòng tổng quát hiện tại do có sự thay đổi về dữ liệu 
						Support.roomList.clear(); 
						
						// Lấy lại danh sách phòng tổng quát từ cơ sở dữ liệu qua lớp DAO_Room
						Support.roomList.addAll(DAO_Room.getInstance().selectALL());
						
						// Đặt lại nội dung hiển thị lên bảng phòng
						setRoomToTable(Support.roomList);
						
						// Thông báo sửa thành công
						Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Sửa thông tin thành công !");
					}else {
						
						Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI SỬA THÔNG TIN", "Không thể sửa mã phòng!");
					}
				}
				else {
					Support.NoticeAlert(AlertType.ERROR,"THÔNG BÁO", "LỖI SỬA THÔNG TIN", "Danh sách phòng trống !"
							+ "\nVui lòng thêm phòng !");
				}
			}catch (Exception e){
				e.printStackTrace();
					
			}
		}
	}
	
	// Phương thức xóa thông tin trên phần nhập thông tin
	@FXML
	public void eraseRoomInTextField() {
		chooseTownComboBox2.setValue(null);
		RoomIDTextField.setText("");
	   	RoomTypeTextField.setText("");
	   	RoomPriceTextField.setText("");
	}
	
	// Phương thức xóa phòng
	@FXML
	public void deleteRoom() {
		// Lấy đối tượng chứa thông tin tại vị trí chọn trên bảng
		Room selection = RoomTableView.getSelectionModel().getSelectedItem();
		// Nếu không có lựa chọn nào
		if(selection == null) {
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", "LỖI XÓA THÔNG TIN", "Không thể xóa phòng !"
	    			+ "\nVui lòng chọn phòng cần xóa trên bảng ");
		}
		// Nếu lựa chọn là phòng đã thuê
		else if("Đã thuê".equals(selection.getStateroom())) {
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", "LỖI XÓA THÔNG TIN", 
					"Không thể xóa tòa phòng đang có khách thuê ! ");
		}
		else {
			// Truy vấn xóa thông tin
			DAO_Room.getInstance().deleteData(selection, "");
		    Support.roomList.remove(selection); // Xóa đối tượng khỏi danh sách
		    RoomTableView.setItems(Support.roomList); // Hiển thị lại danh sách thông tin
		    Support.NoticeAlert(AlertType.INFORMATION, "THÔNG BÁO", null, "Xóa thông tin thành công !");
		}

	}
	/*-------------------------------------------------------------------------*/
	    
	/*---------------------Phương thức mở các Form---------------------*/
	// RoomController sẽ không có phương thức mở RoomForm vì chính RoomForm đang được chạy
	// Mở Form trang chủ
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
	
	// Đăng xuất
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
		rankAccount = Support.pointRank; // Lấy cấp bậc của gói đăng kí
		validRoomTab(Support.homeList); //homeList đã được gọi và lấy dữ liệu từ HomePageController
		
		RoomButton.setStyle(Support.colorButton); // Chỉnh màu nút Phòng khi đang chạy Form
		accountNameLabel.setText(Support.NameAccount); // Hiển thị tên tài khoản đăng nhập
		
		// Tạo đối tượng dang sách ObservableList
		listRoomState = FXCollections.observableArrayList();
		checkListRoom = FXCollections.observableArrayList();
		checkListService = FXCollections.observableArrayList();
		
		// Thêm nội dung vào hộp chọn trạng thái
		chooseStateComboBox.getItems().addAll("Trống", "Đã thuê");
		
		// Thêm nội dung vào hộp chọn tòa 
		for(HomeTown x : Support.homeList) {
			// Lấy nội dung mã tòa và địa chỉ
			chooseTownComboBox.getItems().add(x.toString());
			// Lấy mã tòa
			chooseTownComboBox2.getItems().add(x.getTownID());
		}
		
		setHomeTownToTable(Support.homeList); // Gọi phương thức cập nhật danh sách tòa lên bảng hiển thị
		
		// Xóa danh sách phòng trước đó rồi truy vấn lại phòng trường hợp nội dung danh sách có thay đổi
		Support.roomList.clear();
		Support.roomList.addAll(DAO_Room.getInstance().selectALL());
		setRoomToTable(Support.roomList); // Gọi phương thức cập nhật danh sách phòng lên bảng hiển thị
	    
	}
}