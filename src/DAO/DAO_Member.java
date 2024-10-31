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
import model.Member;

public class DAO_Member implements DAOInterface<Member>{
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
			
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
				
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_Member instance; 
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
    private DAO_Member() {
    	
    }
    public static DAO_Member getInstance() {
    	// Kiểm tra nếu instance chưa được tạo
        if (instance == null) {
        	// Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
            synchronized (DAO_Tenant.class) {
                if (instance == null) {
                	// Tạo mới instance nếu chưa có
                    instance = new DAO_Member();
                }
            }
        }
        return instance;
    }
    
    // Phương thức thêm dữ liệu vào cơ sở dữ liệu
	@Override
	public void insertData(Member t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "INSERT INTO thanhvien (MaTV, MaK, TenTV, GioiTinh, NgaySinh, CCCD, SDT, Que) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, s);
				preparedStatement.setString(2, s1);
				preparedStatement.setString(3, t.getName());
				preparedStatement.setString(4, t.getSex());
				preparedStatement.setDate(5, Date.valueOf(t.getBirthdate()));
				preparedStatement.setString(6, t.getCitizenID());
				preparedStatement.setString(7, t.getPhoneNum());
				preparedStatement.setString(8, t.getPlaceOrigin());
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
	
	// Phương thức xóa dữ liệu vào cơ sở dữ liệu
	@Override
	public void deleteData(Member t, String s) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "DELETE FROM thanhvien WHERE MaTV = ?"; // Lệnh truy vấn

			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn 
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getMemberID());
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
	public void updateData(Member t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "UPDATE thanhvien SET TenTV = ?, GioiTinh = ?, NgaySinh = ?, "
					+ "CCCD = ?, SDT = ?, Que = ? WHERE MaTV = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn 
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getName());
				preparedStatement.setString(2, t.getSex());
				preparedStatement.setDate(3, Date.valueOf(t.getBirthdate()));
				preparedStatement.setString(4, t.getCitizenID());
				preparedStatement.setString(5, t.getPhoneNum());
				preparedStatement.setString(6, t.getPlaceOrigin());
				preparedStatement.setString(7, s);
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
	public ObservableList<Member> selectALL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObservableList<Member> selectByCondition(String condition1, String condition2, String condition3) {
		ObservableList<Member> listMem = FXCollections.observableArrayList();
		Member x;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM thanhvien WHERE MaK = ?"; // Lệnh truy vấn
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
					x = new Member(resultSet.getString("MaTV"),
							resultSet.getString("TenTV"),
							resultSet.getString("GioiTinh"),
							resultSet.getDate("NgaySinh").toLocalDate(),
							resultSet.getString("CCCD"),
							resultSet.getString("SDT"),
							resultSet.getString("Que"));
					listMem.add(x);
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
		return listMem;
	}

	@Override
	public ObservableList<Member> searching(String text, String condition1, String condition2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectCount(String condition1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteAllData(String s1) {
		try { 
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối cơ sở dữ liệu
			String query = "DELETE FROM thanhvien WHERE MaK = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn 
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, s1);
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
	public Member selectObject(String s, String s1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void insertDataToTempTable(Member t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteDataFromTempTable(Member t) {
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
