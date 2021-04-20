package models;

public class User {
	private int user_id;
	private String username;
	private String password;
	private String email;
	private boolean google_user;
	private float balance;
	
	public User(int user_id, String username, String password, String email, boolean google_user, float balance) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.google_user = google_user;
		this.balance = balance;
	}
	
	public User(boolean invalid) {
		this.user_id=-1;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isGoogle_user() {
		return google_user;
	}

	public void setGoogle_user(boolean google_user) {
		this.google_user = google_user;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", google_user=" + google_user + ", balance=" + balance + "]";
	}
}
