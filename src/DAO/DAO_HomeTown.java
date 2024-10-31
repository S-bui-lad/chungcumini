package DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.ConnectionToDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.HomeTown;

public class DAO_HomeTown implements DAOInterface<HomeTown> {
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
		
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
	
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_HomeTown instance;
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
    private DAO_HomeTown() {
    	// Khởi tạo đối tượng DAO_HomeTown
    }
    
    public static DAO_HomeTown getInstance() {
    	// Kiểm tra nếu instance chưa được tạo
        if (instance == null) {
        	// Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
            synchronized (DAO_HomeTown.class) {
                if (instance == null) {
                	// Tạo mới instance nếu chưa có
                    instance = new DAO_HomeTown();
                }
            }
        }
        return instance;
    }
    
    // Phương thức thêm dữ liệu vào cơ sở dữ liệu
	@Override
	public void insertData(HomeTown t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "INSERT INTO toanha (MaT, DiaChi) " 
					+ "VALUES(?, ?)"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getTownID());
				preparedStatement.setString(2, t.getAddress());
				preparedStatement.execute(); // Thực hiện truy vấn
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
	}
	
	// Phương thức xóa dữ liệu vào cơ sở dữ liệu
	@Override
	public void deleteData(HomeTown t, String s) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "DELETE FROM toanha WHERE MaT = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getTownID());
				preparedStatement.execute(); // Thực hiện truy vấn
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
	}
	
	// Phương thức cập nhật dữ liệu vào cơ sở dữ liệu
	@Override
	public void updateData(HomeTown t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "UPDATE toanha SET MaT = ?, DiaChi = ? WHERE MaT = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getTownID());
				preparedStatement.setString(2, t.getAddress());
				preparedStatement.setString(3, t.getTownID());
				preparedStatement.execute(); // Thực hiện truy vấn
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
	}
	
	// Phương thức lấy tất cả dữ liệu trên cơ sở dữ liệu
	@Override
	public ObservableList<HomeTown> selectALL() {
		ObservableList<HomeTown> list = FXCollections.observableArrayList();
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM toanha"; // Lệnh truy vấn
			HomeTown x;
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery(); 
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				while(resultSet.next()) {
					/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
					x = new HomeTown(resultSet.getString("MaT"), resultSet.getString("DiaChi"));
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
		return list;
	}
	
	
	@Override
	public ObservableList<HomeTown> selectByCondition(String condition1, String condition2, String condition3) {
		return null;
	}
	
	// Phương thức lấy số lượng dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public int selectCount(String condition1) {
		int num = 0;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT COUNT(MaT) AS Num FROM toanha "; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery();
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				if(resultSet.next()) {
					// Lấy dữ liệu từ cột Num
					num = resultSet.getInt("Num");
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
		return num;
	}
	
	@Override
	public ObservableList<HomeTown> searching(String text, String condition1, String condition2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllData(String s1) {
		// TODO Auto-generated method stub
		
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
	public HomeTown selectObject(String s, String s1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertDataToTempTable(HomeTown t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDataFromTempTable(HomeTown t) {
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
