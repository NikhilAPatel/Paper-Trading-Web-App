package napatel_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;

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
import static utils.Constants.google_client_id;
import static utils.Utils.queryUserByEmail;
import static utils.Utils.addUser;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

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
		
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
		    // Specify the CLIENT_ID of the app that accesses the backend:
		    .setAudience(Collections.singletonList(google_client_id))
		    // Or, if multiple clients access the backend:
		    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
		    .build();

		// (Receive idTokenString by HTTPS POST)

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(id_token);
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (idToken != null) {
		  Payload payload = idToken.getPayload();
		  String userId = payload.getSubject();
		  
		  String ret = googleSSO(email, userId);
		  System.out.println(ret);
		  out.println(googleSSO(email, userId));
		  return;
		} else {
		  System.out.println("Invalid ID token.");
		  out.println("\"success\":false, \"message\":\"Google SSO Failed\"}");
		}
		out.println("\"success\":false, \"message\":\"Google SSO Failed\"}");
	}
	
	//Accepts the validated email and id_token and either signs up the user or fails and returns JSON data
	public static String googleSSO(String email, String id_token) {
		//Get the User info associated with the email
		User user = queryUserByEmail(email);
		System.out.println(user);
		
		//If the userid is -1, then they do not exist and we can sign them up
		if(user.getUser_id()==-1) {
			System.out.println("Signing up Google User");
			int new_id = addUser(email, id_token);
			return "{\"success\":true, \"message\":\"Signup Success\", \"user_id\":"+new_id+"}";
		}
		
		//If the code is here, then the user is in the database already. First, check if they are a google user
		if(!user.isGoogle_user()) {
			return "{\"success\":false, \"message\":\"Google SSO Failed: You have already registered without Google\"}";
		}
		
		//If the code is here, then the user has successfully logged in
		return "{\"success\":true, \"message\":\"Login Success\", \"user_id\":"+user.getUser_id()+"}";
		
	}
}
