package DAO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.graphbuilder.org.apache.harmony.awt.gl.Crossing.QuadCurve;

import database.ConnectionToDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Bill;

public class DAO_Bill implements DAOInterface<Bill>{
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
			
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
		
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_Bill instance;
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
	private DAO_Bill() {
	    // Khởi tạo đối tượng DAO_HomeTown
	}
	    
	public static DAO_Bill getInstance() {
	// Kiểm tra nếu instance chưa được tạo
		if (instance == null) {
	        // Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
	        synchronized (DAO_Bill.class) {
	           if (instance == null) {
	                // Tạo mới instance nếu chưa có
	                instance = new DAO_Bill();
	                }
	           }
	    }
	    return instance;
	}
	
	// Phương thức thêm dữ liệu vào cơ sở dữ liệu
	@Override
	public void insertData(Bill t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "INSERT INTO hoadon (ID, MaT, MaP, MaK, SoDien, NgayXuatHD, TongTien) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getAccountID());
				preparedStatement.setString(2, t.getHomeTownID());
				preparedStatement.setString(3, t.getRoomID());
				preparedStatement.setString(4, t.getTenantID());
				preparedStatement.setInt(5, t.getElectricNumber());
				preparedStatement.setDate(6, Date.valueOf(t.getInvoiceDate()));
				preparedStatement.setBigDecimal(7, new BigDecimal(t.getBillPrice()));
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
	public void deleteData(Bill t, String s) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "DELETE FROM hoadon WHERE MaHD = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setInt(1, t.getBillID());
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
	public void updateData(Bill t, String s, String s1) {
		// TODO Auto-generated method stub
		
	}
	
	// Phương thức lấy tất cả dữ liệu từ cơ sở dữ liệu
	@Override
	public ObservableList<Bill> selectALL() {
		ObservableList<Bill> list = FXCollections.observableArrayList();
		String query = "SELECT * FROM hoadon";
		Bill x;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			try {
				preparedStatement = connection.prepareStatement(query);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					x = new Bill(resultSet.getInt("MaHD"),
							resultSet.getString("ID"),
							resultSet.getString("MaT"),
							resultSet.getString("MaP"),
							resultSet.getString("MaK"),
							resultSet.getInt("SoDien"),
							resultSet.getDate("NgayXuatHD").toLocalDate(),
							String.valueOf(resultSet.getDouble("TongTien")));
					list.add(x);
				}
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
	
	// Phương thức lấy dữ liệu có điều kiện từ cơ sở dữ liệu
	@Override
	public ObservableList<Bill> selectByCondition(String condition1, String condition2, String condition3) {
		ObservableList<Bill> list = FXCollections.observableArrayList();
		String query = "";
		// Lấy truy vấn theo điều kiện
		if(condition1 != null && condition2 != null && condition3 != null) {
			query = "SELECT * FROM hoadon WHERE MaT = ? AND MaP = ? AND NgayXuatHD = ?";
		}
		else if(condition1 != null && condition2 != null && condition3 == null) {
			query = "SELECT * FROM hoadon WHERE MaT = ? AND MaP = ?";
		}
		else if(condition1 != null && condition2 == null && condition3 != null) {
			query = "SELECT * FROM hoadon WHERE MaT = ? AND NgayXuatHD = ?";
		}
		else if(condition1 != null && condition2 == null && condition3 == null) {
			query = "SELECT * FROM hoadon WHERE MaT = ?";
		}
		else if(condition1 == null && condition2 == null && condition3 != null) {
			query = "SELECT * FROM hoadon WHERE NgayXuatHD = ?";
		}
		Bill x;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt thông tin vào câu truy vấn theo điều kiện
				if(condition1 != null && condition2 != null && condition3 != null) {
					preparedStatement.setString(1, condition1);
					preparedStatement.setString(2, condition2);
					preparedStatement.setString(3, condition3);
				}
				else if(condition1 != null && condition2 != null && condition3 == null) {
					preparedStatement.setString(1, condition1);
					preparedStatement.setString(2, condition2);
				}
				else if(condition1 != null && condition2 == null && condition3 != null) {
					preparedStatement.setString(1, condition1);
					preparedStatement.setString(2, condition3);
				}
				else if(condition1 != null && condition2 == null && condition3 == null) {
					preparedStatement.setString(1, condition1);
				}
				else if(condition1 == null && condition2 == null && condition3 != null) {
					preparedStatement.setString(1, condition3);
				}
				// Thực hiện truy vấn
				resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next()) {
					// Tạo đối tượng tương ứng
					x = new Bill(resultSet.getInt("MaHD"),
							resultSet.getString("ID"),
							resultSet.getString("MaT"),
							resultSet.getString("MaP"),
							resultSet.getString("MaK"),
							resultSet.getInt("SoDien"),
							resultSet.getDate("NgayXuatHD").toLocalDate(),
							String.valueOf(resultSet.getDouble("TongTien")));
					list.add(x);
				}
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
	public ObservableList<Bill> searching(String text, String condition1, String condition2) {
		// TODO Auto-generated method stub
		return null;
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
	public Bill selectObject(String s, String s1) {
		Bill x = new Bill();
		String query = "SELECT * FROM hoadon WHERE MaT = ? AND MaP = ?";
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, s);
				preparedStatement.setString(2, s1);
				resultSet = preparedStatement.executeQuery(); // Thực hiện truy vấn
				while(resultSet.next()) {
					// Tạo đối tượng tương ứng
					x = new Bill(resultSet.getInt("MaHD"),
							resultSet.getString("ID"),
							resultSet.getString("MaT"),
							resultSet.getString("MaP"),
							resultSet.getString("MaK"),
							resultSet.getInt("SoDien"),
							resultSet.getDate("NgayXuatHD").toLocalDate(),
							String.valueOf(resultSet.getDouble("TongTien")));
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
	public void insertDataToTempTable(Bill t) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "INSERT INTO inhoadon (ID, MaT, MaP, MaK, SoDien, NgayXuatHD, TongTien) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getAccountID());
				preparedStatement.setString(2, t.getHomeTownID());
				preparedStatement.setString(3, t.getRoomID());
				preparedStatement.setString(4, t.getTenantID());
				preparedStatement.setInt(5, t.getElectricNumber());
				preparedStatement.setDate(6, Date.valueOf(t.getInvoiceDate()));
				preparedStatement.setBigDecimal(7, new BigDecimal(t.getBillPrice()));
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
	public void deleteDataFromTempTable(Bill t) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "DELETE FROM inhoadon"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
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
	public InputStream getReport(String column_name, String report_name) {
		InputStream input = null;
		String query = "SELECT ? FROM reports WHERE report_name = ?";
		try {
			connection = ConnectionToDatabase.getConnectionDB();
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, column_name);
				preparedStatement.setString(2, report_name);
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next()) {
					input = resultSet.getBinaryStream(1);
				}
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
		return input;
	}

	@Override
	public String selectTurnover(int month, int year) {
		String sumPrice = "0";
		String query = "";
		if(month != -1 && year != -1) {
			query = "Select Sum(TongTien) as TongDoanhThu From hoadon"
					+" where Month(NgayXuatHD) = ? and Year(NgayXuatHD) = ?"
					+" group by Month(NgayXuatHD) ";
		}
		else if(month == -1 && year != -1) {
			query = "Select Sum(TongTien) as TongDoanhThu From hoadon"
					+" where Year(NgayXuatHD) = ?"
					+" group by Year(NgayXuatHD) ";
		}
		try {
			connection = ConnectionToDatabase.getConnectionDB();
			try {
				preparedStatement = connection.prepareStatement(query);
				if(month != -1 && year != -1) {
					preparedStatement.setInt(1, month);
					preparedStatement.setInt(2, year);
				}
				else if(month == -1 && year != -1) {
					preparedStatement.setInt(1, year);
				}
				resultSet = preparedStatement.executeQuery();
				while(resultSet.next()) {
					sumPrice = String.valueOf(resultSet.getBigDecimal("TongDoanhThu"));
					if(sumPrice.isEmpty()) {
						sumPrice = "0";
					}
				}
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
		return sumPrice;
	}
	
}
