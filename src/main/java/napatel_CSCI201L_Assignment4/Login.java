package napatel_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

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

import static utils.Constants.tiingo_token;
import static utils.Utils.queryUser;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Login() {
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
		
		//Get User data associated with username
		User user = queryUser(username);
		
		//If no username could be found, tell them to sign up
		if(user.getUser_id()==-1) {
			out.println("{\"success\":false, \"message\":\"Login Failed: User does not exist, please sign up\"}");
			return;
		}
		
		//If the user is a google user, tell them to use that button
		if(user.isGoogle_user()) {
			out.println("{\"success\":false, \"message\":\"Login Failed: You have signed up with Google, please use that to sign in\"}");
			return;
		}
		
		//Check if the password is right
		if(!user.getPassword().equals(password)) {
			out.println("{\"success\":false, \"message\":\"Login Failed: Incorrect password\"}");
			return;
		}
		
		//If the code is here, then the user should probably be logged in
		out.println("{\"success\":true, \"message\":\"Login Success\", \"user_id\": "+user.getUser_id()+"}");
	}
}
