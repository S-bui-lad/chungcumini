package model;

public class AccountLogin {
	private String userID; // ID đăng nhập tài khoản
	private String password; // Mật khẩu tài khoản
	private String accountname; // Tên người dùng tài khoản
	private int rank; // Cấp bậc tài khoản (phân theo gói dịch vụ tài khoản đăng kí)
	
	// Phương thức khởi tạo mặc định
	public AccountLogin() {
		
	}
	
	// Phương thức khởi tạo có tham số
	public AccountLogin(String userID, String password, String accountname, int rank) {
		super();
		this.userID = userID;
		this.password = password;
		this.accountname = accountname;
		this.rank = rank;
	}
	
	// Getter và Setter
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String checkRank(int check_rank) {
		if(check_rank == 0) {
			return "Gói miễn phí";
		} else if (check_rank == 1) {
			return "Gói cơ bản 1";
		} else if(check_rank == 2) {
			return "Gói cơ bản 2";
		} else if(check_rank == 3) {
			return "Gói cao cấp 1";
		} else if(check_rank == 4) {
			return "Gói cao cấp 2";
		}
		return "Gói kim cương";
	}
}
