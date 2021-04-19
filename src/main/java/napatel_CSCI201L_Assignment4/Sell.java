package napatel_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import static utils.Constants.dbAddress;
import static utils.Utils.getStockAttribute;
import static utils.Utils.getUserBalance;
import static utils.Utils.marketClosed;
import static utils.Utils.getTimestamp;
import static utils.Utils.round;
import static utils.Utils.getOwnedStock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.OwnedStock;
import models.Stock;
import models.StockMeta;

//TODO look into SQL date/timestamp types
//TODO check if quantity box is empty

@WebServlet("/buy")
public class Sell extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Sell() {
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
	}

	//Receives userId, ticker, and quantity and buys the stock
	//Either returns {success: boolean, message: String}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		
		
		//Parameter Acquisition
		String ticker = request.getParameter("ticker");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		
		//Check if the quantity is invalid
		if(quantity<1) {
			out.println("{\"success\": false, \"message\": \"FAILED: Purchase not possible\"}"); //TODO lint
			return;//TODO can you do this in Java
		}
		
		//Return error if the market is closed
		if(marketClosed()) {
			out.println("{\"success\": false, \"message\": \"FAILED: Market is closed\"}"); //TODO lint
			return;//TODO can you do this in Java
		}
		
			
		//Do sell
		float bid = getStockAttribute("bid");
		out.println(sell(user_id, ticker, bid, quantity));
				
	}
	
	
	//Returns JSON string that doGet will return to client
	public String sell(int user_id, String ticker, float bid, int quantity) {
		//Initialization
		float balance = getUserBalance(user_id);
		Connection conn = null;
		PreparedStatement ps = null;
		int rs;
		
		//get all of this stock that this user owns
		ArrayList<OwnedStock> ownedStock = getOwnedStock(user_id, ticker);
		
		//Make sure that the user owns enough stock to do the sale
		if(ownedStock.size()==0) {
			return "{\"success\": false, \"message\": \"FAILED: Purchase not possible\"}"; 
		}
		int totalStockUnits = 0;
		for(OwnedStock s: ownedStock) {
			totalStockUnits += s.getQuantity();
		}
		if(totalStockUnits<quantity) {
			return "{\"success\": false, \"message\": \"FAILED: Cannot sell more stock than you own\"}";
		}
		
		//Calculate the user's new balance
		float newBalance = balance+(bid*quantity);
		
		//Do the actual sale
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			
			ps = conn.prepareStatement("");
			
			rs = ps.executeUpdate();
			
			if(rs==0) {//It probably failed
				return "{\"success\": false, \"message\": \"FAILED\"}";//TODO lint and check if this actually happens when it fails
			}
			
			//Return success message
			return "{\"success\": true, \"message\": \"SUCCESS: Executed sale of 2 shares of "+ticker+" for $"+round(bid)+"\"}";//TODO lint
			
			
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
		
		
		//If the code is here, we probably failed
		return "{\"success\": false, \"message\": \"FAILED\"}";//TODO lint
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}