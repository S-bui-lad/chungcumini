/* Đây là lớp trung gian giúp hỗ trợ việc trao đổi dữ liệu giữa các Form trở nên 
   dễ dàng hơn. Dữ liệu được đổ vào các thuộc tính lưu trữ và được tái sử dụng lại. */

package application;

import java.util.Set;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Bill;
import model.HomeTown;
import model.Member;
import model.Room;
import model.RoomAndTenant;
import model.Service;
import model.Tenant;

public class Support {
	public static int pointRank; // Thuộc tính lưu trữ số cấp bậc của tài khoản đăng nhập
	public static String IDAccount; // Thuộc tính lưu trữ mã của tài khoản đăng nhập
	public static String NameAccount; // Thuộc tính lưu trữ tên riêng của tài khoản đăng nhập
	public static String rankAccount; // Thuộc tính lưu trữ tên cấp bậc của tài khoản đăng nhập
	public static ObservableList<HomeTown> homeList; // Danh sách tổng quát các tòa nhà
    public static ObservableList<Room> roomList; // Danh sách tổng quát các phòng
    public static ObservableList<Tenant> tenantList; // Danh sách tổng quát khách thuê phòng
    
    // Danh sách thành viên ở cùng khách thuê, mỗi khách sẽ có 1 danh sách thành viên khác nhau
    public static ObservableList<Member> currMemberList; 
    
    // Danh sách lưu trữ truy vấn thông tin tất cả từ phòng và khách tại phòng đó 
    public static ObservableList<RoomAndTenant> RoomAndTenantList; 
    
    // Danh sách lưu trữ truy vấn thông tin dịch vụ 
    public static ObservableList<Service> serviceList; 
    
    // Danh sách lưu trữ truy vấn hóa đơn
    public static ObservableList<Bill> billList; 
    
    //Cửa sổ, nội dung hiển thị của chương trình chính - không phải của phần đăng nhập
    public static Parent root; // Đối tượng Parent - nút gốc quy định bố cục trên Scene
    public static Stage stage; // Đối tượng Stage - cửa sổ của chương trình chính - không phải của
    public static Scene scene; // Đối tượng Scene chứa nội dung của 1 cửa sổ đồ họa
    public static Image icon; // Đối tượng chứa hình ảnh, đây sẽ biểu tượng (icon) của phần mềm
    
    // Thuộc tính tinh chỉnh màu của Button đại diện cho Form đang được mở
    public static String colorButton = "-fx-background-color:#137b9e"; 
    
    // Tạo đối tượng giúp thao tác với các thông báo
    public static Alert alert;
    
    // Phương thức hiển thị thông báo cho các Form chính của chương trình
    public static void NoticeAlert(AlertType type, String title, String header, String content) {
    	alert = new Alert(type);
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    alert.showAndWait();
    }
}
