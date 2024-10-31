
/*  --------------------------- Controller của Form khách ---------------------------  */

package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import DAO.DAO_Room;
import DAO.DAO_RoomAndTenant;
import DAO.DAO_Tenant;
import application.Support;
import application.TitleForms;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.HomeTown;
import model.Room;
import model.RoomAndTenant;

public class TenantController implements Initializable{
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
	// 1. Form hiển thị danh sách các phòng theo ô
	@FXML
	private AnchorPane MainRoomForm; // Form hiển thị danh sách các phòng theo ô
	@FXML
	private TextField searchRoomTextField; // Nơi nhập nội dung tìm kiếm phòng
	@FXML
	private ComboBox<String> chooseTownComboBox; // Hộp chọn tòa nhà tại Form đầu tiên
	@FXML
	private ComboBox<String> chooseStateComboBox; // Hộp chọn trạng thái phòng
	@FXML
	private GridPane menuRoomGridPane; // Form hiển thị 1 lưới các danh sách phòng
	/*----------------*/
	
	// 2.Form hiển thị bảng danh sách khách thuê phòng
	@FXML
	private AnchorPane viewTenantForm; // Form hiển thị bảng danh sách khách thuê phòng
	@FXML
	private TextField searchTenantTextField; // Nơi nhập nội dung tìm kiếm khách
	@FXML
	private ComboBox<String> chooseTownComboBox1; // Hộp chọn tòa nhà tại Form thứ 2
	@FXML
    private TableView<RoomAndTenant> InforTenantTableView; // Bảng hiển thị danh sách tổng hợp khách thuê
	@FXML
    private TableColumn<RoomAndTenant, LocalDate> birthTenant_col; // Cột ngày sinh
	@FXML
    private TableColumn<RoomAndTenant, String> citizenIDTenant_col; // Cột căn cước
    @FXML
    private TableColumn<RoomAndTenant, String> homeID_col; // Cột mã tòa
    @FXML
    private TableColumn<RoomAndTenant, String> nameTenant_col; // Cột tên khách
    @FXML
    private TableColumn<RoomAndTenant, Integer> order; // Cột số thứ tự
    @FXML
    private TableColumn<RoomAndTenant, String> phoneNumTenant_col; // Cột số điện thoại
    @FXML
    private TableColumn<RoomAndTenant, String> placeTenant_col; // Cột quê quán
    @FXML
    private TableColumn<RoomAndTenant, LocalDate> rentdateTenant_col; // Cột ngày thuê
    @FXML
    private TableColumn<RoomAndTenant, String> roomID_col; // Cột mã phòng
    @FXML
    private TableColumn<RoomAndTenant, String> sexTenant_col; // Cột giới tính
    @FXML
    private TableColumn<RoomAndTenant, String> tenantID_col; // Cột mã khách
    /*----------------------------------------------------------*/
    
    /*---------------------Khai báo hỗ trợ---------------------*/
    // Danh sách phòng lấy ra từ bộ lọc hoặc được tìm kiếm
	private ObservableList<Room> listRoomState;
	// Danh sách tổng hợp khách và phòng lấy ra từ bộ lọc hoặc được tìm kiếm
	private ObservableList<RoomAndTenant> searchListTenant;
	/*-------------------------------------------------------------*/
	
	/*---------------------Phương thức chính của file--------------------*/
	/*---------------------------Form đầu tiên---------------------------*/
	
	/* Phương thức thiết lập hiển thị danh sách các phòng theo ô, phương thức này sẽ chịu
	 trách nhiệm lấy dữ liệu từ file cardRoom được thiết kế để hiển thị ô phòng và thêm ô
	 vào danh sách lưới */
	public void setRoomMenu(ObservableList<Room> list) {
		// Xóa ràng buộc từng hàng, cột để thiết lập dữ liệu hiển thị theo ý muốn
		menuRoomGridPane.getChildren().clear();
		menuRoomGridPane.getRowConstraints().clear();
		menuRoomGridPane.getColumnConstraints().clear();
		int column = 0, row = 0;
		for(int i = 0; i < list.size(); i++) {
			try {
				// Tạo đối tượng FXMLLoader
				FXMLLoader loader = new FXMLLoader();
				// Thiết lập đường dẫn tới file FXML để tải dữ liệu từ nó
				loader.setLocation(getClass().getResource("/designFXML/cardRoom.fxml"));
				// Tải dữ liệu từ file FXML đã thiết lập đường dẫn
				AnchorPane pane = loader.load();
				// Lấy controller của file FXML đã thiết lập
				cardRoomController cardR = loader.getController();
				// Gọi phương thức của cardRoomController để truyền dữ liệu vào ô hiển thị Phòng
				cardR.setRoom(list.get(i)); 
				
				// Nếu sô cột = 4 thì xuống dòng và thiết lập lại số cột = 0
				if(column == 4) {
					column = 0;
					row += 1;
				}
				// Thêm dữ liệu pane vào Form lưới
				menuRoomGridPane.add(pane, column++, row); 
				GridPane.setMargin(pane, new Insets(11)); // Thiết lập khoảng cách giữa các ô phòng
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	// Phương thức lọc phòng theo lựa chọn từ hộp lựa chọn (Nút ấn "Lọc")
	@FXML
	public void filterMenuRoom() {
		// Nếu lựa chọn từ hộp trạng thái là rỗng và hộp tòa nhà khác rỗng
		if(chooseStateComboBox.getValue() == null && 
				chooseTownComboBox.getValue() != null) {
			String town = chooseTownComboBox.getValue(); //Lấy lựa chọn từ ComboBox
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu - có trong chuỗi
			listRoomState.clear(); // Xóa dữ liệu trong danh sách phòng có điều kiện
			// Truy vấn danh sách phòng có điều kiện trong cơ sở dữ liệu qua lớp DAO_Room
			listRoomState.addAll(DAO_Room.getInstance().selectByCondition(codeHomeTownID[0], null, null));
			setRoomMenu(listRoomState); // Thiết lập hiển thị lại danh sách các ô phòng
		}
		// Nếu lựa chọn từ hộp tòa nhà và hộp trạng thái phòng đều rỗng
		else if(chooseStateComboBox.getValue() == null && 
				chooseTownComboBox.getValue() == null) {
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", null, "Vui lòng chọn 1 trong 2 mục !");
		}
		// Nếu lựa chọn từ hộp trạng thái khác rỗng và hộp tòa nhà là rỗng
		else if(chooseStateComboBox.getValue() != null && 
				chooseTownComboBox.getValue() == null) {
			listRoomState.clear();
			// Kiểm tra nội dung lựa chọn và truy vấn theo lựa chọn đó
			if("Trống".equals(chooseStateComboBox.getValue())) {
				//Gọi truy vấn cơ sở dữ liệu thông qua lớp trung gian DAO
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(null, "0", null));
			}
			else if("Đã thuê".equals(chooseStateComboBox.getValue())) {
				//Gọi truy vấn cơ sở dữ liệu thông qua lớp trung gian DAO
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(null, "1", null));
			}
			setRoomMenu(listRoomState); // Thiết lập hiển thị lại danh sách các ô phòng
		}
		else if(chooseStateComboBox.getValue() != null && 
				chooseTownComboBox.getValue() != null) {
			String town = chooseTownComboBox.getValue(); //Lấy lựa chọn từ ComboBox
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu - có trong chuỗi
			listRoomState.clear();
			if("Trống".equals(chooseStateComboBox.getValue())) {
				//Gọi truy vấn cơ sở dữ liệu thông qua lớp trung gian DAO
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(codeHomeTownID[0], "0", null));
			}
			else if("Đã thuê".equals(chooseStateComboBox.getValue())) {
				//Gọi truy vấn cơ sở dữ liệu thông qua lớp trung gian DAO
				listRoomState.addAll(DAO_Room.getInstance().selectByCondition(codeHomeTownID[0], "1", null));
			}
			setRoomMenu(listRoomState); // Thiết lập hiển thị lại danh sách các ô phòng
		}
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
		setRoomMenu(listRoomState); // Thiết lập hiển thị lại danh sách các ô phòng
	}
	
	// Phương thức làm mới lại nội dung trên bảng (Nút ấn có hình vòng tròn 2 mũi tên )
	@FXML
	public void refreshRoom() {
		listRoomState.clear(); // Xóa dữ liệu từ danh sách phòng được tìm kiếm/lọc theo điều kiện
		chooseTownComboBox.setValue(null); // Đặt lại lựa chọn trên hộp chọn tòa nhà là rỗng
		chooseStateComboBox.setValue(null); // Đặt lại lựa chọn trên hộp chọn trạng thái là rỗng
		// Đặt lại nội dung hiển thị từ danh sách phòng tổng quát lên lưới hiển thị
		setRoomMenu(Support.roomList); 
	}
	
	// Phương thức mở Form chứa bảng danh sách tất cả các khách thuê
	@FXML
	public void showViewTenantForm() {
		MainRoomForm.setVisible(false);
		viewTenantForm.setVisible(true);
	}
	
	// Phương thức mở lại Form ban đầu
	@FXML
	public void backMainRoomForm() {
		MainRoomForm.setVisible(true);
		viewTenantForm.setVisible(false);
	}
	/*-------------------------*/
	
	/*---------------------------Form thứ 2---------------------------*/
	@FXML
	public void searchListTenant() {
		// Nếu danh sách đã chứa dữ liệu tìm kiếm trước đó
		if(searchListTenant.size() > 0) {
			searchListTenant.clear(); // Xóa dữ liệu tồn
		}
		if(chooseTownComboBox1.getValue() == null) {
			// Truy vấn tìm kiếm
			searchListTenant.addAll(DAO_RoomAndTenant.getInstance().
					searching(searchTenantTextField.getText(),"", ""));
		}
		else {
			String town = chooseTownComboBox1.getValue(); //Lấy lựa chọn từ ComboBox
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu - có trong chuỗi
			searchListTenant.addAll(DAO_RoomAndTenant.getInstance().
					searching(searchTenantTextField.getText(),codeHomeTownID[0], ""));
		}
		setInforTenantTable(searchListTenant); // Thiết lập lại thông tin hiển thị lên bảng
	}
	
	
	// Phương thức làm mới lại nội dung trên bảng (Nút ấn có hình vòng tròn 2 mũi tên )
	@FXML
	public void refreshListTenant() {
		// Xóa dữ liệu từ danh sách chứa thông tin tìm kiếm 
		searchListTenant.clear();
		chooseTownComboBox1.setValue(null); // Đặt lại lựa chọn trên hộp chọn tòa nhà là rỗng
		// Đặt lại nội dung hiển thị từ danh sách phòng và khách tổng quát lên bảng hiển thị
		setInforTenantTable(Support.RoomAndTenantList);
	}
	
	// Phương thức xem danh sách theo lựa chọn từ hộp lựa chọn (Nút ấn "Xem")
	@FXML
	public void showListTenantRoom() {
		if(chooseTownComboBox1.getValue() == null) {
			Support.NoticeAlert(AlertType.ERROR, "THÔNG BÁO", null, "Vui lòng chọn tòa nhà !");
		}
		else {
			String town = chooseTownComboBox1.getValue(); //Lấy lựa chọn từ ComboBox
			String[] codeHomeTownID = town.split("-"); //Tách chuỗi từ dấu - có trong chuỗi
			Support.RoomAndTenantList.clear();
			// Truy vấn theo điều kiện
			Support.RoomAndTenantList.addAll(
					DAO_RoomAndTenant.getInstance().selectByCondition(codeHomeTownID[0], "", ""));
			// Hiển thị danh sách vừa tìm kiếm lên bảng
			setInforTenantTable(Support.RoomAndTenantList); 
		}
	}
	
	// Phương thức hỗ trợ tạo file Excel từ danh sách 
	public void saveExcelFile(ObservableList<RoomAndTenant> list, File file) {
	    // Tạo workbook với định dạng .xlsx (với phiên bản cũ là .xls sẽ là HSSFWorkbook)
	    Workbook workbook = new XSSFWorkbook();
	    // Tạo sheet
	    Sheet sheet = workbook.createSheet();

	    // Tạo CellStyle cho tiêu đề (chữ đậm)
	    CellStyle headerStyle = workbook.createCellStyle();
	    Font font = workbook.createFont(); // Tạo đối tượng Font
	    font.setBold(true); // Đặt chữ đậm
	    headerStyle.setFont(font);

	    // Màu nền của các ô tiêu đề
	    headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    // Tạo 1 dòng đầu tiên để chứa tiêu đề các cột
	    Row headerRow = sheet.createRow(0);

	    // Tạo các cột và áp dụng cell style
	    for (int i = 0; i <= 10; i++) {
	    	// Tạo ô ứng với cột tiêu đề thứ i
	        Cell cell = headerRow.createCell(i); 
	        // Lấy tiêu đề cho ô thứ i từ phương thức getHeaderTitle
	        cell.setCellValue(getHeaderTitle(i));
	        // Định dạng màu nền cho ô tiêu đề
	        cell.setCellStyle(headerStyle);
	    }
	    // Thêm dữ liệu từ TableView vào Excel
	    RoomAndTenant roomAndTenant; 
	    for (int i = 0; i < list.size(); i++) {
	        roomAndTenant = list.get(i); // Lấy dữ liệu đối tượng từ danh sách
	        Row row = sheet.createRow(i + 1); // Tạo dòng để chứa thông tin
	        
	        // Đặt số thứ tự của dữ liệu vào ô thứ 0 của dòng
	        row.createCell(0).setCellValue(i + 1); 
	        
	        // Đặt mã tòa vào ô thứ 1 của dòng
	        row.createCell(1).setCellValue(roomAndTenant.getHome().getTownID()); 
	        
	        // Đặt mã phòng vào ô thứ 2 của dòng
	        row.createCell(2).setCellValue(roomAndTenant.getRoom().getRoomID());
	        
	        // Đặt mã khách vào ô thứ 3 của dòng
	        row.createCell(3).setCellValue(roomAndTenant.getTenant().getTenantID());
	        
	        // Đặt tên khách vào ô thứ 4 của dòng
	        row.createCell(4).setCellValue(roomAndTenant.getTenant().getName());
	        
	        // Đặt giới tính khách vào ô thứ 5 của dòng
	        row.createCell(5).setCellValue(roomAndTenant.getTenant().getSex());
	        
	        // Đặt ngày sinh khách vào ô thứ 6 của dòng
	        row.createCell(6).setCellValue(roomAndTenant.getTenant().getBirthdate());
	        
	        // Đặt mã căn cước khách vào ô thứ 7 của dòng
	        row.createCell(7).setCellValue(roomAndTenant.getTenant().getCitizenID());
	        
	        // Đặt số điện thoại khách vào ô thứ 8 của dòng
	        row.createCell(8).setCellValue(roomAndTenant.getTenant().getPhoneNum());
	        
	        // Đặt quê quán khách vào ô thứ 9 của dòng
	        row.createCell(9).setCellValue(roomAndTenant.getTenant().getPlaceOrigin());
	        
	        // Đặt ngày thuê phòng vào ô thứ 10 của dòng
	        row.createCell(10).setCellValue(roomAndTenant.getTenant().getRentDate());
	    }
	    // Lưu workbook vào tệp Excel bằng cách tạo đối tượng FileOutputStream để ghi dữ liệu
	    try (FileOutputStream fileOut = new FileOutputStream(file)) {
	        workbook.write(fileOut); // Ghi workbook vào FileOutputStream.
	        fileOut.close(); // Đóng FileOutputStream
	        workbook.close(); // Đóng Workbook
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	private String getHeaderTitle(int index) {
	    // Lấy tên cho cột dựa trên chỉ số
	    switch (index) {
	        case 0: return "STT";
	        case 1: return "Mã tòa nhà";
	        case 2: return "Mã phòng";
	        case 3: return "Mã khách";
	        case 4: return "Tên khách";
	        case 5: return "Giới tính";
	        case 6: return "Ngày sinh";
	        case 7: return "CCCD";
	        case 8: return "Số điện thoại";
	        case 9: return "Quê quán";
	        case 10: return "Ngày thuê";
	        default: return "";
	    }
	}
	
	// Phương thức lưu danh sách thông tin khách vào tệp Excel (Nút ấn Xuất danh sách ra Excel)
	@FXML
	public void convertExcel() {
		// Tạo đối tượng FileChooser để mở cửa sổ chọn nơi lưu tệp Excel
	    FileChooser chooseFile = new FileChooser();
	    
	    /*Lấy ngày tháng lưu tệp*/
        LocalDate currentDate = LocalDate.now(); // Lấy ngày tháng hiện tại
        // Định dạng theo mẫu "dd-MM-yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // Chuyển đổi ngày tháng thành chuỗi theo định dạng
        String formattedDate = currentDate.format(formatter);
        
        if(chooseTownComboBox1.getValue() == null) {
        	// Thiết lập tên mặc định cho file Excel
    	    chooseFile.setInitialFileName("Danh sách chủ thuê phòng " + formattedDate);
		}
        else {
        	// Thiết lập tên danh sách theo tòa cho tệp Excel
        	chooseFile.setInitialFileName(
        			"Danh sách chủ thuê phòng toà " + chooseTownComboBox1.getValue()
        			+ " " + formattedDate);
        }
	    
	    // Lọc ra tệp excel và chỉ hiển thị định dạng tệp excel từ cửa sổ chọn nơi lưu tệp
	    FileChooser.ExtensionFilter excelFilter = new 
	    		FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
	    chooseFile.getExtensionFilters().add(excelFilter);

	    // Tạo cửa sổ hiển thị File Chooser
	    Stage showFileChooser = new Stage();
	    // Hiển thị cửa sổ lựa chọn nơi lưu tệp và trả về 1 file
	    File file = chooseFile.showSaveDialog(showFileChooser);
	    // Nếu đối tượng File tồn tại thì thực hiện gọi phương thức hỗ trợ truyền dữ liệu vào Excel
	    if (file != null) {
	        saveExcelFile(Support.RoomAndTenantList, file);
	    }
	}
	
	// Thiết lập hiển thị cho bảng
	public void setInforTenantTable(ObservableList<RoomAndTenant> list) {
		// Thiết lập hiển thị lên cột số thứ tự dựa trên vị trí của đối tượng trong danh sách.
		order.setCellValueFactory(cellData -> 
		new SimpleIntegerProperty(list.indexOf(cellData.getValue()) + 1).asObject());
		// Thiết lập hiển thị cột mã tòa
		homeID_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getHome().getTownID()));
		// Thiết lập hiển thị cột mã phòng
		roomID_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getRoom().getRoomID()));
		// Thiết lập hiển thị cột mã khách
		tenantID_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getTenant().getTenantID()));
		// Thiết lập hiển thị cột tên khách
		nameTenant_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getTenant().getName()));
		// Thiết lập hiển thị cột giới tính
		sexTenant_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getTenant().getSex()));
		// Thiết lập hiển thị cột ngày sinh
		birthTenant_col.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getTenant().getBirthdate()));
		// Thiết lập hiển thị cột căn cước
		citizenIDTenant_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getTenant().getCitizenID()));
		// Thiết lập hiển thị cột số điện thoại
		phoneNumTenant_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getTenant().getPhoneNum()));
		// Thiết lập hiển thị cột nơi sinh
		placeTenant_col.setCellValueFactory(cellData -> 
		new SimpleStringProperty(cellData.getValue().getTenant().getPlaceOrigin()));
		// Thiết lập hiển thị cột ngày thuê
		rentdateTenant_col.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getTenant().getRentDate()));
		// Đặt dữ liệu từ danh sách các đối tượng lên bảng hiển thị 
		InforTenantTableView.setItems(list); 

	}
	
	/*---------------------Phương thức mở các Form---------------------*/
	// TenantController sẽ không có phương thức mở TenantForm vì chính TenantForm đang được chạy
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
		
		listRoomState = FXCollections.observableArrayList();
		searchListTenant = FXCollections.observableArrayList();
		
		Support.RoomAndTenantList = FXCollections.observableArrayList();
		// Truy vấn lấy thông tin của danh sách khách thuê cùng mã phòng
		Support.RoomAndTenantList.addAll(DAO_RoomAndTenant.getInstance().selectALL());
		setInforTenantTable(Support.RoomAndTenantList);
		
		Support.tenantList = FXCollections.observableArrayList();
		// Truy vấn lấy thông tin của danh sách khách thuê 
		Support.tenantList.addAll(DAO_Tenant.getInstance().selectALL());
		
		// Xóa danh sách phòng trước đó rồi truy vấn lại phòng trường hợp nội dung danh sách có thay đổi
		Support.roomList.clear();
		Support.roomList.addAll(DAO_Room.getInstance().selectALL());
		setRoomMenu(Support.roomList);
		
		// Thêm nội dung vào hộp chọn tòa 
		chooseStateComboBox.getItems().addAll("Trống", "Đã thuê");
		for(HomeTown x : Support.homeList) {
			chooseTownComboBox.getItems().add(x.toString());
			chooseTownComboBox1.getItems().add(x.toString());
		}
	}
	
}
