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
import model.Room;
import model.RoomAndTenant;
import model.Tenant;

public class DAO_RoomAndTenant implements DAOInterface<RoomAndTenant>{
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
				
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
				
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_RoomAndTenant instance;
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
    private DAO_RoomAndTenant() {
    	
    }

    public static DAO_RoomAndTenant getInstance() {
    	// Kiểm tra nếu instance chưa được tạo
        if (instance == null) {
        	// Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
            synchronized (DAO_RoomAndTenant.class) {
                if (instance == null) {
                	// Tạo mới instance nếu chưa có
                    instance = new DAO_RoomAndTenant();
                }
            }
        }
        return instance;
    }
	@Override
	public void insertData(RoomAndTenant t, String s, String s1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteData(RoomAndTenant t, String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateData(RoomAndTenant t, String s, String s1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ObservableList<RoomAndTenant> selectALL() {
		ObservableList<RoomAndTenant> list = FXCollections.observableArrayList();
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM khachthue"; // Lệnh truy vấn
			RoomAndTenant x;
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn 
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				while(resultSet.next()) {
					/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
					x = new RoomAndTenant(new HomeTown(resultSet.getString("MaT"),""),
							new Room(resultSet.getString("MaP"), "", "", ""),
							new Tenant(resultSet.getString("MaK"), 
									resultSet.getString("TenK"), 
									resultSet.getString("GioiTinh"), 
									resultSet.getDate("NgaySinh").toLocalDate(),
									resultSet.getString("CCCD"),
									resultSet.getString("SDT"),
									resultSet.getString("Que"),
									resultSet.getDate("NgayThue").toLocalDate()));
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
	public ObservableList<RoomAndTenant> selectByCondition(String condition1, String condition2, String condition3) {
		ObservableList<RoomAndTenant> list = FXCollections.observableArrayList();
		RoomAndTenant x;
		String query = "SELECT * FROM khachthue WHERE MaT = ?"; // Lệnh truy vấn
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
					x = new RoomAndTenant(new HomeTown(resultSet.getString("MaT"),""),
							new Room(resultSet.getString("MaP"), "", "", ""),
							new Tenant(resultSet.getString("MaK"), 
									resultSet.getString("TenK"), 
									resultSet.getString("GioiTinh"), 
									resultSet.getDate("NgaySinh").toLocalDate(),
									resultSet.getString("CCCD"),
									resultSet.getString("SDT"),
									resultSet.getString("Que"),
									resultSet.getDate("NgayThue").toLocalDate()));
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
	public ObservableList<RoomAndTenant> searching(String text, String condition1, String condition2) {
		ObservableList<RoomAndTenant> list = FXCollections.observableArrayList();
	    RoomAndTenant x;
	    String query = "";
	    // Tùy điều kiện mà lựa chọn câu lệnh truy vấn
	    if("".equals(condition1)) {
	    	query = "SELECT * FROM khachthue WHERE MaK LIKE ? OR "
	    			+ "MaP LIKE ? OR "
	    			+ "MaT LIKE ? OR "
	        		+ "TenK LIKE ? OR "
	        		+ "GioiTinh LIKE ? OR "
	        		+ "NgaySinh LIKE ? OR "
	        		+ "CCCD LIKE ? OR "
	        		+ "SDT LIKE ? OR "
	        		+ "Que LIKE ? OR "
	        		+ "NgayThue LIKE ?";
	    }
	    else {
	    	query = "SELECT * FROM khachthue WHERE (MaT = ?) AND "
	    			+ "(MaK LIKE ? OR "
	    			+ "MaP LIKE ? OR "
	        		+ "TenK LIKE ? OR "
	        		+ "GioiTinh LIKE ? OR "
	        		+ "NgaySinh LIKE ? OR "
	        		+ "CCCD LIKE ? OR "
	        		+ "SDT LIKE ? OR "
	        		+ "Que LIKE ? OR "
	        		+ "NgayThue LIKE ?)";
	    }
	    try {
	        connection = ConnectionToDatabase.getConnectionDB(); // Đóng kết nối
	        try {
	        	// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự tùy điều kiện
	            if("".equals(condition1)) {
	            	preparedStatement.setString(1, "%" + text + "%");
		            preparedStatement.setString(2, "%" + text + "%");
		            preparedStatement.setString(3, "%" + text + "%");
		            preparedStatement.setString(4, "%" + text + "%");
		            preparedStatement.setString(5, "%" + text + "%");
		            preparedStatement.setString(6, "%" + text + "%");
		            preparedStatement.setString(7, "%" + text + "%");
		            preparedStatement.setString(8, "%" + text + "%");
		            preparedStatement.setString(9, "%" + text + "%");
		            preparedStatement.setString(10, "%" + text + "%");
	            }
	            else {
	            	preparedStatement.setString(1, condition1);
		            preparedStatement.setString(2, "%" + text + "%");
		            preparedStatement.setString(3, "%" + text + "%");
		            preparedStatement.setString(4, "%" + text + "%");
		            preparedStatement.setString(5, "%" + text + "%");
		            preparedStatement.setString(6, "%" + text + "%");
		            preparedStatement.setString(7, "%" + text + "%");
		            preparedStatement.setString(8, "%" + text + "%");
		            preparedStatement.setString(9, "%" + text + "%");
		            preparedStatement.setString(10, "%" + text + "%");
	            }
	            /* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
	            resultSet = preparedStatement.executeQuery(); 
	            // Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
	            while (resultSet.next()) {
	            	/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
	            	x = new RoomAndTenant(new HomeTown(resultSet.getString("MaT"),""),
							new Room(resultSet.getString("MaP"), "", "", ""),
							new Tenant(resultSet.getString("MaK"), 
									resultSet.getString("TenK"), 
									resultSet.getString("GioiTinh"), 
									resultSet.getDate("NgaySinh").toLocalDate(),
									resultSet.getString("CCCD"),
									resultSet.getString("SDT"),
									resultSet.getString("Que"),
									resultSet.getDate("NgayThue").toLocalDate()));
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
	public int selectCount(String condition1) {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoomAndTenant selectObject(String s, String s1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertDataToTempTable(RoomAndTenant t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDataFromTempTable(RoomAndTenant t) {
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
