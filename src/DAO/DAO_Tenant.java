package DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.ConnectionToDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AccountLogin;
import model.Tenant;

public class DAO_Tenant implements DAOInterface<Tenant>{
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
			
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
			
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_Tenant instance; 
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
    private DAO_Tenant() {
    	
    }
    public static DAO_Tenant getInstance() {
    	// Kiểm tra nếu instance chưa được tạo
        if (instance == null) {
        	// Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
            synchronized (DAO_Tenant.class) {
                if (instance == null) {
                	// Tạo mới instance nếu chưa có
                    instance = new DAO_Tenant();
                }
            }
        }
        return instance;
    }
    
    // Phương thức thêm dữ liệu vào cơ sở dữ liệu
	@Override
	public void insertData(Tenant t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			// 2 lệnh truy vấn
			String query1 = "INSERT INTO khachthue (MaK, MaP, MaT, TenK, GioiTinh, NgaySinh, CCCD, "
					+ "SDT, Que, NgayThue) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			String query2 = "UPDATE phong SET TrangThai = 1 WHERE MaP = ? AND MaT = ?";
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn Query1
				preparedStatement = connection.prepareStatement(query1);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getTenantID());
				preparedStatement.setString(2, s);
				preparedStatement.setString(3, s1);
				preparedStatement.setString(4, t.getName());
				preparedStatement.setString(5, t.getSex());
				preparedStatement.setDate(6, Date.valueOf(t.getBirthdate()));
				preparedStatement.setString(7, t.getCitizenID());
				preparedStatement.setString(8, t.getPhoneNum());
				preparedStatement.setString(9, t.getPlaceOrigin());
				preparedStatement.setDate(10, Date.valueOf(t.getRentDate()));
				preparedStatement.execute(); // Thực hiện truy vấn
				
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn Query2
				preparedStatement = connection.prepareStatement(query2);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, s);
				preparedStatement.setString(2, s1);
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
	public void deleteData(Tenant t, String s) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			// 2 lệnh truy vấn
			String query1 = "DELETE FROM khachthue WHERE MaK = ?";
			String query2 = "UPDATE phong SET TrangThai = 0 WHERE MaP = ?";
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn Query1
				preparedStatement = connection.prepareStatement(query1);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getTenantID());
				preparedStatement.execute(); // Thực hiện truy vấn
				
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn Query2
				preparedStatement = connection.prepareStatement(query2);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, s);
				preparedStatement.execute(); // Thực hiện truy vấn
				
				ConnectionToDatabase.closeConnection(connection); //Đóng kết nối
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
	public void updateData(Tenant t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "UPDATE khachthue SET MaK = ?, TenK = ?, GioiTinh = ?, NgaySinh = ?, "
					+ "CCCD = ?, SDT = ?, Que = ?, NgayThue = ? WHERE MaK = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn 
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getTenantID());
				preparedStatement.setString(2, t.getName());
				preparedStatement.setString(3, t.getSex());
				preparedStatement.setDate(4, Date.valueOf(t.getBirthdate()));
				preparedStatement.setString(5, t.getCitizenID());
				preparedStatement.setString(6, t.getPhoneNum());
				preparedStatement.setString(7, t.getPlaceOrigin());
				preparedStatement.setDate(8, Date.valueOf(t.getRentDate()));
				preparedStatement.setString(9, s);
				// Thực hiện truy vấn
				preparedStatement.execute();
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
	public ObservableList<Tenant> selectALL() {
		ObservableList<Tenant> listTenant = FXCollections.observableArrayList();
		Tenant x;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM khachthue";
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
					x = new Tenant(resultSet.getString("MaK"), resultSet.getString("TenK"),
							resultSet.getString("GioiTinh"),
							resultSet.getDate("NgaySinh").toLocalDate(),
							resultSet.getString("CCCD"),
							resultSet.getString("SDT"),
							resultSet.getString("Que"),
							resultSet.getDate("NgayThue").toLocalDate());
					listTenant.add(x);
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
		return listTenant;
	}

	@Override
	public ObservableList<Tenant> selectByCondition(String condition1, String condition2, String condition3) {
		ObservableList<Tenant> listTenant = FXCollections.observableArrayList();
		Tenant x;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM khachthue WHERE MaP = ? AND MaT = ?";	// Lệnh truy vấn	
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
					 dữ liệu từ bảng đã truy vấn của ResultSet */
					x = new Tenant(resultSet.getString("MaK"), resultSet.getString("TenK"),
							resultSet.getString("GioiTinh"),
							resultSet.getDate("NgaySinh").toLocalDate(),
							resultSet.getString("CCCD"),
							resultSet.getString("SDT"),
							resultSet.getString("Que"),
							resultSet.getDate("NgayThue").toLocalDate());
					listTenant.add(x);
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
		return listTenant;
	}
	@Override
	public int selectCount(String condition1) {
		int num = 0;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			// Lệnh truy vấn
			String query = "SELECT COUNT(*) AS Totals"
					+ " FROM (SELECT MaK FROM khachthue UNION ALL SELECT MaK FROM thanhvien) as CM ";
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn 
				preparedStatement = connection.prepareStatement(query);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
	            resultSet = preparedStatement.executeQuery(); 
	            // Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				if(resultSet.next()) {
					num = resultSet.getInt("Totals");
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
	public ObservableList<Tenant> searching(String text, String condition1, String condition2) {
		ObservableList<Tenant> list = FXCollections.observableArrayList();
		Tenant x;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM khachthue WHERE MaK LIKE ? OR" 
			        + " TenK LIKE ? OR"
			        + " GioiTinh LIKE ? OR"
			        + " NgaySinh = ? OR"
			        + " CCCD LIKE ? OR"
			        + " SDT LIKE ? OR"
			        + " Que LIKE ? OR"
			        + " NgayThue = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn 
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
			    preparedStatement.setString(1, "%" + text + "%"); 
			    preparedStatement.setString(2, "%" + text + "%"); 
			    preparedStatement.setString(3, "%" + text + "%"); 
			    preparedStatement.setString(4, text); 
			    preparedStatement.setString(5, "%" + text + "%"); 
			    preparedStatement.setString(6, "%" + text + "%"); 
			    preparedStatement.setString(7, "%" + text + "%"); 
			    preparedStatement.setString(8, text); 

			    /* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
	            resultSet = preparedStatement.executeQuery(); 
	            // Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				if(resultSet.next()) {
					/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
					x = new Tenant(resultSet.getString("MaK"), resultSet.getString("TenK"),
							resultSet.getString("GioiTinh"),
							resultSet.getDate("NgaySinh").toLocalDate(),
							resultSet.getString("CCCD"),
							resultSet.getString("SDT"),
							resultSet.getString("Que"),
							resultSet.getDate("NgayThue").toLocalDate());
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
	public String selectID(String s1, String s2) {
		String ID = "";
		String query = "SELECT MaK FROM khachthue WHERE MaT = ? AND MaP = ?";
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, s1);
				preparedStatement.setString(2, s2);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery();
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				if(resultSet.next()) {
					ID = resultSet.getString("MaK");
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
		return ID;
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
	public Tenant selectObject(String s, String s1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void insertDataToTempTable(Tenant t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteDataFromTempTable(Tenant t) {
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
