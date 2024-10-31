package DAO;


import java.io.InputStream;

import javafx.collections.ObservableList;

public interface DAOInterface<T> {
	// Phương thức thêm thông tin vào cơ sở dữ liệu
	public void insertData(T t, String s, String s1);
	
	// Phương thức xóa thông tin vào cơ sở dữ liệu
	public void deleteData(T t, String s);
	
	// Phương thức sửa thông tin vào cơ sở dữ liệu
	public void updateData(T t, String s, String s1);
	
	// Phương thức lấy tất cả thông tin từ cơ sở dữ liệu
	public ObservableList<T> selectALL();
	
	// Phương thức lấy thông tin từ cơ sở dữ liệu theo điều kiện
	public ObservableList<T> selectByCondition(String condition1, String condition2, String condition3);
	
	// Phương thức tìm kiếm thông tin từ cơ sở dữ liệu
	public ObservableList<T> searching(String text, String condition1, String condition2);
	
	// Phương thức lấy số lượng thông tin từ cơ sở dữ liệu
	public int selectCount(String condition1);
	
	// Phương thức lấy ID từ cơ sở dữ liệu
	public String selectID(String s1, String s2);
	
	// Phương thức xóa tất cả thông tin từ cơ sở dữ liệu
	public void deleteAllData(String s1);
	
	// Phương thức lấy ra 1 đối tượng dữ liệu từ cơ sở dữ liệu
	public T selectObject(String s, String s1);
	
	// Phương thức lấy tổng tiền (bên DAO_Service)
	public String sumPrice(String condition1, String condition2);
	
	// Phương thức thêm thông tin hóa đơn vào bảng phụ để chứa hóa đơn
	public void insertDataToTempTable(T t);
	
	// Phương thức xóa thông tin hóa đơn từ bảng phụ 
	public void deleteDataFromTempTable(T t);
	
	// Phương thức lấy nguồn dữ liệu từ bảng Report trong cơ sở dữ liệu
	public InputStream getReport(String report_name,String column_name);
	
	// Phương thức lấy doanh thu theo tháng và năm từ cơ sở dữ liệu
	public String selectTurnover(int month, int year);
}
