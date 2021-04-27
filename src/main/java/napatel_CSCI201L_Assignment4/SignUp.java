package napatel_CSCI201L_Assignment4;

import static utils.Utils.addUser;
import static utils.Utils.queryUser;
import static utils.Utils.queryUserByEmail;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.User;

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

		// Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		// Get user data associated with the username
		User user = queryUser(username);

		// Get the user data associated with the email
		User email_user = queryUserByEmail(email);

		// Make sure that this user is not a google user
		if (user.isGoogle_user() || email_user.isGoogle_user()) {
			out.println(
					"{\"success\":false, \"message\":\"Signup Failed: Account has already registered with Google\"}");
			return;
		}

		// Make sure that no user with the same email exists
		if (email_user.getUser_id() != -1) {
			out.println("{\"success\":false, \"message\":\"Signup Failed: Account with username already exists\"}");
			return;
		}

		// Make sure that no user with the same username exists
		if (user.getUser_id() != -1) {
			out.println("{\"success\":false, \"message\":\"Signup Failed: Account with username already exists\"}");
			return;
		}

		// Make sure that no user with the same email exists
		user = queryUserByEmail(email);
		if (user.getUser_id() != -1) {
			out.println("{\"success\":false, \"message\":\"Signup Failed: Account with email already exists\"}");
			return;
		}

		// Add user info to the database
		int new_id = addUser(username, email, password);
		if (new_id != -1) {
			out.println("{\"success\":true, \"message\":\"Signup Success\", \"user_id\":" + new_id + "}");
		} else {
			out.println("{\"success\":false, \"message\":\"Signup Failed\"}");
		}

	}
}
