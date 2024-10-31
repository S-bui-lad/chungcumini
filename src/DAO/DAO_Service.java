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
import model.Service;

public class DAO_Service implements DAOInterface<Service>{
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
			
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
			
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_Service instance;
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
    private DAO_Service() {
    	// Khởi tạo đối tượng DAO_Room
    }
    public static DAO_Service getInstance() {
    	// Kiểm tra nếu instance chưa được tạo
        if (instance == null) {
        	// Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
            synchronized (DAO_Service.class) {     
                if (instance == null) {
                	// Tạo mới instance nếu chưa có
                    instance = new DAO_Service();
                }
            }
        }
        return instance;
    }
    
 // Phương thức thêm dữ liệu vào cơ sở dữ liệu
	@Override
	public void insertData(Service t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "INSERT INTO dichvu (MaDV, MaT, TenDV, LoaiDV, GiaDV) "
					+ "VALUES(?, ?, ?, ?, ?)"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getIDService());
				preparedStatement.setString(2, s);
				preparedStatement.setString(3, t.getNameService());
				preparedStatement.setString(4, t.getTypeService());
				preparedStatement.setDouble(5, Double.parseDouble(t.getPriceService()));
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

	@Override
	public void deleteData(Service t, String s) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "DELETE FROM dichvu WHERE MaDV = ? AND MaT = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getIDService());
				preparedStatement.setString(2, s);
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

	@Override
	public void updateData(Service t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "UPDATE dichvu SET TenDV = ?, LoaiDV = ?, "
					+ "GiaDV = ? WHERE MaDV = ? AND MaT = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getNameService());
				preparedStatement.setString(2, t.getTypeService());
				preparedStatement.setDouble(3, Double.parseDouble(t.getPriceService()));
				preparedStatement.setString(4, s);
				preparedStatement.setString(5, s1);
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

	@Override
	public ObservableList<Service> selectALL() {
		ObservableList<Service> list = FXCollections.observableArrayList();
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM dichvu"; // Lệnh truy vấn
			Service x;
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
					x = new Service(new HomeTown(resultSet.getString("MaT"), ""), 
							resultSet.getString("MaDV"), 
							resultSet.getString("TenDV"), 
							resultSet.getString("LoaiDV"),
							String.valueOf(resultSet.getDouble("GiaDV")));
					list.add(x);
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
	public ObservableList<Service> selectByCondition(String condition1, String condition2, String condition3) {
		ObservableList<Service> list = FXCollections.observableArrayList();
		Service x;
		String query = "SELECT * FROM dichvu WHERE MaT = ?"; // Lệnh truy vấn
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, condition1);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery();
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				while(resultSet.next()) {
					/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
					x = new Service(new HomeTown(resultSet.getString("MaT"), ""), 
							resultSet.getString("MaDV"), 
							resultSet.getString("TenDV"), 
							resultSet.getString("LoaiDV"),
							String.valueOf(resultSet.getDouble("GiaDV")));
					list.add(x);
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
	public ObservableList<Service> searching(String text, String condition1, String condition2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectCount(String condition1) {
		int num = 0;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT COUNT(MaT) AS Num FROM dichvu "; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery();
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				if(resultSet.next()) {
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
	public String selectID(String s1, String s2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllData(String s1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String sumPrice(String condition1, String condition2) {
		double sum = 0;
		String query = "";
		if(condition2.charAt(0) != '!') {
			query = "SELECT Sum(GiaDV) AS SUM FROM dichvu WHERE MaT = ?"
					+ " AND LoaiDV = ?";
		}
		else {
			query = "SELECT Sum(GiaDV) AS SUM FROM dichvu WHERE MaT = ?"
					+ " AND LoaiDV <> ?";
		}
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, condition1);
				preparedStatement.setString(2, condition2.replace("!", ""));
				resultSet = preparedStatement.executeQuery();
				while(resultSet.next()) {
					sum = resultSet.getDouble("SUM");
				}
			} catch(SQLException e) {
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
		return String.valueOf(sum);
	}
	@Override
	public Service selectObject(String s, String s1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void insertDataToTempTable(Service t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteDataFromTempTable(Service t) {
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
