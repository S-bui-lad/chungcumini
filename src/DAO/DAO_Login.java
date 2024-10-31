package DAO;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.ConnectionToDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AccountLogin;

public class DAO_Login implements DAOInterface<AccountLogin> {
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
	
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
	
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_Login instance;
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
    private DAO_Login() {
    	// Khởi tạo đối tượng DAO_Login
    }
    
    
    public static DAO_Login getInstance() {
    	// Kiểm tra nếu instance chưa được tạo
        if (instance == null) {
        	// Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
            synchronized (DAO_Login.class) {
                if (instance == null) {
                	// Tạo mới instance nếu chưa có
                    instance = new DAO_Login();
                }
            }
        }
        return instance;
    }
    
    // Phương thức thêm dữ liệu vào cơ sở dữ liệu
	@Override
	public void insertData(AccountLogin account, String s, String s1) {

	}
	
	// Phương thức xóa dữ liệu vào cơ sở dữ liệu
	@Override
	public void deleteData(AccountLogin account, String s) {
		
	}
	
	// Phương thức cập nhật dữ liệu vào cơ sở dữ liệu
	@Override
	public void updateData(AccountLogin account, String s, String s1) {
		
	}
	
	// Phương thức lấy tất cả dữ liệu trên cơ sở dữ liệu
	@Override
	public ObservableList<AccountLogin> selectALL() {
		return null;
	}
	
	// Phương thức lọc dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public ObservableList<AccountLogin> selectByCondition(String condition1, String condition2, String condition3) {
		ObservableList<AccountLogin> list = FXCollections.observableArrayList();
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			// Lệnh truy vấn
			String query = "SELECT * FROM dangnhap WHERE ID = ? AND MatKhau = ?";
			AccountLogin x;
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query); 
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, condition1); 
				preparedStatement.setString(2, condition2);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery(); 
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				while(resultSet.next()) {
					/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
					x = new AccountLogin(resultSet.getString("ID"),
							resultSet.getString("MatKhau"), 
							resultSet.getString("TenNguoiDung"),
							resultSet.getInt("CapBac"));
					list.add(x); // Thêm vào danh sách
				}
				ConnectionToDatabase.closeConnection(connection); // Đóng kết nối
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Đóng các kết nối các dữ liệu đã sử dụng
            if (resultSet != null) {
                try {
					resultSet.close(); // Đóng kết nối
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
            if (preparedStatement != null) {
                try {
					preparedStatement.close(); // Đóng kết nối
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
		}
		return list;
	}
	
	// Phương thức lấy số lượng của dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public int selectCount(String condition1) {
	
		return 0;
	}
	
	// Phương thức tìm kiếm dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public ObservableList<AccountLogin> searching(String text, String condition1, String condition2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Phương thức xóa tất cả dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public void deleteAllData(String s1) {
		// TODO Auto-generated method stub
		
	}
	
	// Phương thức lấy 1 đối tượng dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public AccountLogin selectObject(String s, String s1) {
		AccountLogin x = new AccountLogin();
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM dangnhap WHERE ID = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, s); // Đặt nội dung cho dấu ? theo thứ tự
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery(); 
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				if(resultSet.next()) {
					/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
					 x = new AccountLogin(resultSet.getString("ID"), 
							resultSet.getString("MatKhau"), 
							resultSet.getString("TenNguoiDung"), 
							resultSet.getInt("CapBac"));
				}
				ConnectionToDatabase.closeConnection(connection); // Đóng kết nối
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Đóng các kết nối các dữ liệu đã sử dụng
            if (resultSet != null) {
                try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
            if (preparedStatement != null) {
                try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
		}
		return x;
	}


	@Override
	public String sumPrice(String condition1, String condition2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String selectID(String s1, String s2) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void insertDataToTempTable(AccountLogin t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteDataFromTempTable(AccountLogin t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public InputStream getReport(String report_name, String column_name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String selectTurnover(int month, int year) {
		// TODO Auto-generated method stub
		return null;
	}
}
