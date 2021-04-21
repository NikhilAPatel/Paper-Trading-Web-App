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

import static utils.Constants.dbAddress;
import static utils.Utils.getStockAttribute;
import static utils.Utils.getUserBalance;
import static utils.Utils.marketClosed;
import static utils.Utils.getTimestamp;
import static utils.Utils.round;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Stock;
import models.StockMeta;

@WebServlet("/buy")
public class Buy extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Buy() {
		super();
	}
	
	//TODO check if quantity box is empty

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
			out.println("{\"success\":˙ false, \"message\": \"FAILED: Purchase not possible\"}"); //TODO lint
			return;//TODO can you do this in Java
		}
		//TODO uncomment
//		//Return error if the market is closed
//		if(marketClosed()) {
//			out.println("{\"success\": false, \"message\": \"FAILED: Market is closed\"}"); //TODO lint
//			return;//TODO can you do this in Java
//		}
		
			
		//Do buy
		float ask = (float) getStockAttribute(ticker, "ask");
		if(ask==-1) {
			out.println("{\"success\": false, \"message\": \"FAILED\"}");
		}
		out.println(buy(user_id, ticker, ask, quantity));
				
	}
	
	//Returns JSON string that doGet will return to client
	public String buy(int user_id, String ticker, float ask, int quantity) {
		//Initialization
		float balance = getUserBalance(user_id);
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		int rs;
		int rs2;
		String timestamp = getTimestamp();
		
		//Checks if user has enough funds to execute purchase
		boolean canBuy = balance>=ask*quantity;
		if(!canBuy) {//failed
			return "{\"success\": false, \"message\": \"FAILED: Insufficient Balance\"}";//TODO lint
		}
		
		//Calculate the user's new balance
		float newBalance = balance - (ask*quantity);
		
		System.out.println("buying "+ticker+" x"+quantity+" at "+ask);
		
		//Do the actual buy
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			//Add the stock to the owned_stock table
			ps = conn.prepareStatement("insert into Owned_Stock (user_id, stock_id, quantity, purchase_price, timestamp) values (?, (select stock_id from Stock where ticker = ?), ?, ?, ?)");
			ps.setInt(1, user_id);
			ps.setString(2, ticker);
			ps.setInt(3, quantity);
			ps.setFloat(4, ask);
			ps.setString(5, timestamp);
			
			rs = ps.executeUpdate();
			
			if(rs==0) {//It probably failed
				return "{\"success\": false, \"message\": \"FAILED\"}";
			}
			
			//Update the user's balance
			ps2 = conn.prepareStatement("update User set balance = ? where user_id=?");
			ps2.setFloat(1, newBalance);
			ps2.setInt(2,  user_id);
			
			rs2  = ps2.executeUpdate();
			
			if(rs2==0) {//It probably failed
				return "{\"success\": false, \"message\": \"FAILED\"}";
			}
			
			//Return success message
			return "{\"success\": true, \"message\": \"SUCCESS: Executed purchase of 2 shares of "+ticker+" for $"+round(ask)+"\"}";
			
			
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
		return "{\"success\": false, \"message\": \"FAILED\"}";
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}