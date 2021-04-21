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

@WebServlet("/GoogleLogin")
public class GoogleLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GoogleLogin() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String email = request.getParameter("email");
		String id_token = request.getParameter("id_token");
		
		System.out.println("google login server");
	}
}
