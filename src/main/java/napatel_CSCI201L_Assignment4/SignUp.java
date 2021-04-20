package napatel_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Stock;
import models.StockMeta;
import models.User;

import static utils.Constants.dbAddress;
import static utils.Constants.tiingo_token;
import static utils.Utils.queryUser;
import static utils.Utils.queryUserByEmail;

@WebServlet("/signup")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SignUp() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		
		//Make sure that no user with the same username exists
		User user = queryUser(username);
		if(user.getUser_id()!=-1) {
			out.println("{\"success\":false, \"message\":\"Signup Failed: Account with username already exists\"}");
			return;
		}
		
		//Make sure that no user with the same email exists
		user = queryUserByEmail(email);
		if(user.getUser_id()!=-1) {
			out.println("{\"success\":false, \"message\":\"Signup Failed: Account with email already exists\"}");
			return;
		}
		
		//Add user info to the database
		int new_id = addUser(username, email, password);
		if(new_id!=-1) {
			out.println("{\"success\":true, \"message\":\"Signup Success\", \"user_id\":"+new_id+"}");
		}else {
			out.println("{\"success\":false, \"message\":\"Signup Failed\"}");
		}
		
		
	}
	
	public int addUser(String username, String email, String password) {
		//Initialization
		Connection conn = null;
		PreparedStatement ps = null;
		int rs;
		int new_id = -1;
		
		//Do the actual sell
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			//Get all of this user's owned stock of this ticker
			ps = conn.prepareStatement("insert into User (username, password, email, google_user, balance) values (?,?,?,0,50000)");
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, email);
			
			rs = ps.executeUpdate();
			
			User user = queryUser(username);
			new_id = user.getUser_id();
			
		} catch (SQLException | ClassNotFoundException sqle) {
			sqle.printStackTrace();
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return new_id;
	}
}
