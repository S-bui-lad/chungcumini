package DAO;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.ConnectionToDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Room;

public class DAO_Room implements DAOInterface<Room>{
	// Các đối tượng giúp tương tác với cơ sở dữ liệu
	Connection connection; // Đối tượng giúp kết nối tới cơ sở dữ liệu
	PreparedStatement preparedStatement; // Đối tượng giúp truy tới cơ sở dữ liệu
	ResultSet resultSet; // Đối tượng lưu trữ kết quả truy vấn
		
	/* Triển khai thiết kế Double-Checked-Locking (DCL) kết hợp Singleton để tối ưu hiệu suất 
	trong môi trường đa luồng, đảm bảo chỉ có duy nhất 1 instance trong đa luồng*/
		
	// Từ khóa volatile giúp các luồng thấy được sự thay đổi của instance
	private static volatile DAO_Room instance;
	// Chặn việc tạo thêm instance từ bên ngoài lớp bằng từ khóa private
    private DAO_Room() {
    	// Khởi tạo đối tượng DAO_Room
    }

    public static DAO_Room getInstance() {
    	// Kiểm tra nếu instance chưa được tạo
        if (instance == null) {
        	// Kiểm tra lại instance để tránh tạo nhiều instance khi nhiều luồng truy cập vào
            synchronized (DAO_Room.class) {
                if (instance == null) {
                	// Tạo mới instance nếu chưa có
                    instance = new DAO_Room();
                }
            }
        }
        return instance;
    }
    
    // Phương thức thêm dữ liệu vào cơ sở dữ liệu
	@Override
	public void insertData(Room t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "INSERT INTO phong (MaP, MaT, LoaiP, TrangThai, GiaP) "
					+ "VALUES(?, ?, ?, 0, ?)"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getRoomID());
				preparedStatement.setString(2, s);
				preparedStatement.setString(3, t.getTyperoom());
				preparedStatement.setString(4, t.getPriceroom());
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
	public void deleteData(Room t, String s) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "DELETE FROM phong WHERE MaP = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getRoomID());
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
	public void updateData(Room t, String s, String s1) {
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "UPDATE phong SET MaP = ?, MaT = ?, LoaiP = ?, "
					+ "GiaP = ? WHERE MaP = ?"; // Lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt nội dung cho dấu ? theo thứ tự
				preparedStatement.setString(1, t.getRoomID());
				preparedStatement.setString(2, s);
				preparedStatement.setString(3, t.getTyperoom());
				preparedStatement.setString(4, t.getPriceroom());
				preparedStatement.setString(5, t.getRoomID());
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
	public ObservableList<Room> selectALL() {
		ObservableList<Room> list = FXCollections.observableArrayList();
		String statement;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			String query = "SELECT * FROM phong"; // Lệnh truy vấn
			Room x;
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery();
				// Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
				while(resultSet.next()) {
					if(resultSet.getByte("TrangThai") == 0) {
						statement = "Trống";
					} else statement = "Đã thuê";
					/* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
					x = new Room(resultSet.getString("MaP"), 
							resultSet.getString("LoaiP"),
							statement,
							String.valueOf(resultSet.getDouble("GiaP")));
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
	
	// Phương thức lọc dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public ObservableList<Room> selectByCondition(String condition1, String condition2, String condition3) {
		ObservableList<Room> listroom = FXCollections.observableArrayList();
		Room x;
		String statement, query = "";
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			// Lấy truy vấn theo điều kiện
			if(condition1 != null && condition2 == null) {
				// Lệnh truy vấn lấy thông tin phòng với điều kiện là mã tòa
				query = "SELECT * FROM phong WHERE "
						+ "MaT = ?"; 
			}
			else if(condition1 == null && condition2 != null) {
				// Lệnh truy vấn lấy thông tin phòng với điều kiện là trạng thái phòng
				query = "SELECT * FROM phong WHERE "
						+ "TrangThai = ?";
			}
			else {
				// Lệnh truy vấn lấy thông tin phòng với điều kiện là mã tòa và trạng thái phòng
				query = "SELECT * FROM phong WHERE "
						+ "MaT = ? AND TrangThai = ?";
			}
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt thông tin vào câu truy vấn theo điều kiện
				if (condition1 != null && condition2 == null) {
					preparedStatement.setString(1, condition1);
	            }
				else if(condition1 == null && condition2 != null) {
					preparedStatement.setString(1, condition2);
				}
				else {
					preparedStatement.setString(1, condition1);
					preparedStatement.setString(2, condition2);
				}
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery();
				// Duyệt dữ liệu, tạo đối tượng tương ứng và thêm vào danh sách trả về
				while(resultSet.next()) {
					if(resultSet.getByte("TrangThai") == 0) {
						statement = "Trống";
					} else statement = "Đã thuê";
					// Khởi tạo đối tương ứng
					x = new Room(resultSet.getString("MaP"), resultSet.getString("LoaiP"),
							statement, String.valueOf(resultSet.getDouble("GiaP")));
					listroom.add(x);
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
		return listroom;
	}
	
	// Phương thức lấy số lượng của dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public int selectCount(String condition1) {
		int num = 0;
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Két nối cơ sở dữ liệu
			String query = "SELECT COUNT(MaP) AS Num FROM phong"; // Lệnh truy vấn chung
			// Nếu có điều kiện
			if(condition1 != null) {
				query += " WHERE TrangThai = ?"; // Thêm dòng truy vấn
			}
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				// Đặt thông tin vào câu truy vấn theo điều kiện
				if(condition1 != null) {
					preparedStatement.setString(1, condition1);
				}
				resultSet = preparedStatement.executeQuery();
				if(resultSet.next()) {
					num = resultSet.getInt("Num");
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
		return num;
	}
	
	// Phương thức lấy mã ID dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public String selectID(String s1, String s2) {
		String ID = "";
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối cơ sở dữ liệu
			String query = "SELECT MaT FROM phong WHERE MaP = ?"; // lệnh truy vấn
			try {
				// Tạo đối tượng PreparedStatement và truyền tham số truy vấn
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, s1);
				/* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
				resultSet = preparedStatement.executeQuery();
				// Duyệt dữ liệu, tạo đối tượng tương ứng và thêm vào danh sách trả về
				if(resultSet.next()) {
					ID = resultSet.getString("MaT");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
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
	
	// Phương thức tìm kiếm dữ liệu theo điều kiện từ cơ sở dữ liệu
	@Override
	public ObservableList<Room> searching(String text, String condition1, String condition2) {
	    ObservableList<Room> list = FXCollections.observableArrayList();
	    Room x;
	    String statement, query = "";
	    String temp; 
	    String temp1 = "Trống", temp2 = "Đã thuê", temp3 = "Thuê", 
	    		temp4 = "Thue", temp5 = "Da thue", temp6 = "Trong"; 
		// So sánh từng text (bỏ qua viết hoa)
	    if (temp1.equalsIgnoreCase(text) || temp6.equalsIgnoreCase(text)) {
	    	temp = "0";
	    } else if (temp2.equalsIgnoreCase(text) || temp3.equalsIgnoreCase(text)
	    		|| temp4.equalsIgnoreCase(text) || temp5.equalsIgnoreCase(text)) {
	    	temp = "1"; // Phòng đã thuê - dữ liệu 1 trên cơ sở dữ liệu tại cột TrangThai
	    } else {
	    	temp = text;
	    }
	    // Tùy theo điều kiện mà Query sẽ được truyền dữ liệu
	    if("".equals(condition1) && "".equals(condition2)) {
	    	query = "SELECT * FROM phong WHERE MaP LIKE ? OR "
	        		+ "LoaiP LIKE ? OR "
	        		+ "TrangThai LIKE ? OR "
	        		+ "GiaP LIKE ?";
	    }
	    else if("".equals(condition1) && !"".equals(condition2)) {
	    	query = "SELECT * FROM phong WHERE (TrangThai = ?) AND (MaP LIKE ? OR "
	        		+ "LoaiP LIKE ? OR "
	        		+ "GiaP LIKE ?)";
	    }
	    else if (!"".equals(condition1) && "".equals(condition2)) {
	    	query = "SELECT * FROM phong WHERE (MaT = ?) AND (MaP LIKE ? OR "
	        		+ "LoaiP LIKE ? OR "
	        		+ "TrangThai LIKE ? OR "
	        		+ "GiaP LIKE ?)";
	    }
	    else if (!"".equals(condition1) && !"".equals(condition2)){
	    	query = "SELECT * FROM phong WHERE (MaT = ? AND TrangThai = ?) "
 	        		+ "AND (MaP LIKE ? OR "
 	        		+ "LoaiP LIKE ? OR "
 	        		+ "GiaP LIKE ?)";
	    }
	    try {
	        connection = ConnectionToDatabase.getConnectionDB(); // Kết nối cơ sở dữ liệu
	        try {
	        	// Tùy vào điều kiện mà thiết lập dữ liệu truyền cho Query để truy vấn
	            preparedStatement = connection.prepareStatement(query);
	            if("".equals(condition1) && "".equals(condition2)) {
	            	preparedStatement.setString(1, "%" + text + "%");
		            preparedStatement.setString(2, "%" + text + "%");
		            preparedStatement.setString(3, "%" + temp + "%");
		            preparedStatement.setString(4, "%" + text + "%");
	            }
	            else if("".equals(condition1) && !"".equals(condition2)) {
	            	preparedStatement.setString(1, condition2);
		            preparedStatement.setString(2, "%" + text + "%");
		            preparedStatement.setString(3, "%" + text + "%");
		            preparedStatement.setString(4, "%" + text + "%");
	            }
	            else if(!"".equals(condition1) && "".equals(condition2)) {
		            preparedStatement.setString(1, condition1);
		            preparedStatement.setString(2, "%" + text + "%");
		            preparedStatement.setString(3, "%" + text + "%");
		            preparedStatement.setString(4, "%" + text + "%");
		            preparedStatement.setString(5, "%" + text + "%");
	            }
	            else if(!"".equals(condition1) && !"".equals(condition2)){
		            preparedStatement.setString(1, condition1);
		            preparedStatement.setString(2, condition2);
		            preparedStatement.setString(3, "%" + text + "%");
		            preparedStatement.setString(4, "%" + text + "%");
		            preparedStatement.setString(5, "%" + text + "%");
	            }
	            /* Thực hiện truy vấn và trả về đối tượng ResultSet chứa dữ liệu 
				 đã được truy vấn từ câu lệnh Query */
	            resultSet = preparedStatement.executeQuery(); 
	            // Duyệt thông tin từng dòng của bảng dữ liệu đã truy vấn
	            while (resultSet.next()) {
	                if (resultSet.getByte("TrangThai") == 0) {
	                    statement = "Trống";
	                } else {
	                    statement = "Đã thuê";
	                }
	                /* Tạo đối tượng và truyền vào tham số tương ứng với 
					 dữ liệu từ bảng đã truy vấn của ResultSet*/
	                x = new Room(
	                    resultSet.getString("MaP"),
	                    resultSet.getString("LoaiP"),
	                    statement,
	                    String.valueOf(resultSet.getDouble("GiaP"))
	                );
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
	public void deleteAllData(String s1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String sumPrice(String condition1, String condition2) {
		double sum = 0;
		String query = "SELECT Sum(GiaP) AS SUM FROM phong WHERE MaT = ?"
				+ " AND MaP = ?";
		try {
			connection = ConnectionToDatabase.getConnectionDB(); // Kết nối tới cơ sở dữ liệu
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, condition1);
				preparedStatement.setString(2, condition2);
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
	public Room selectObject(String s, String s1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertDataToTempTable(Room t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDataFromTempTable(Room t) {
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
