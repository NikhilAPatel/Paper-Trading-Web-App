package utils;

import static utils.Constants.dbAddress;
import static utils.Constants.tiingo_token;
import static utils.Constants.market_open;
import static utils.Constants.market_close;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.OwnedStock;
import models.Stock;

public final class Utils {

	public static final float round(float num) {
		final DecimalFormat f = new DecimalFormat("##.00");
		return Float.parseFloat(f.format(num));
	}
	
	//TODO: Probably works, but bid and ask are 0 when market is closed
	//Accepts the string of the attribute you want and returns it
	public static final Object getStockAttribute(String ticker, String attribute) throws IOException {
		URL url = new URL("https://api.tiingo.com/iex/" + ticker + "?token=" + tiingo_token);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");

		// Case when ticker does not exist
		if (con.getResponseCode() == 404) {
			System.out.println(ticker + " 218937892does not exist. All involved trades will be purged.");
			return -1;
		} else {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				Gson gson = new GsonBuilder().create();
				Stock stock = gson.fromJson(String.valueOf(response).replace("[", "").replace("]", ""), Stock.class);
				switch(attribute) {
					case "ask":
						return stock.getAskPrice();
					case "bid":
						return stock.getBidPrice();
					case "timestamp":
						return stock.getTimestamp();
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(ticker + " 2837219873does not exist. All involved trades will be purged.");
				return -1;
			}
		}
		
		return -1;
	}
	
	public static final float getUserBalance(int user_id) {
		//Initialization
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs;
		float balance=-1;
		
		//Fetch the balance
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			//Get all of this user's owned stock of this ticker
			ps = conn.prepareStatement("select balance from User where user_id = ?");
			ps.setInt(1, user_id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				balance = rs.getFloat("balance");
			}
			
			
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
		
		if(balance !=-1) {
			return balance;
		}
		
		return 0;
	}
	
	public static final boolean marketClosed() throws IOException {
		//For now, just check if we are between 6:30am PDT and 1:00PM PDT on Monday-Friday
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
		
		//If day of the week is 1 or 7, the market is closed for sure
		if(day_of_week==1 || day_of_week==7) {
			return true;
		}
		
		//If the time is outside of 6:30am PDT and 1:00pm PDT, the market is closed
		int current_time = cal.get(Calendar.HOUR_OF_DAY)*60+cal.get(Calendar.MINUTE);
		if(current_time<market_open || current_time>=market_close) {
			return true;
		}
		
		return true;
		
	}
	//TODO also look into making a method that returns an actual Date timestamp or something
	public static final String getTimestamp() {
		//TODO
		
		return "";
	}
	
	public static final ArrayList<OwnedStock> getOwnedStock(int user_id, String ticker){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs;
		ArrayList<OwnedStock> results = new ArrayList<OwnedStock>();
		
		//Do the actual sell
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			//Get all of this user's owned stock of this ticker
			ps = conn.prepareStatement("select * from owned_stock where user_id = ? and stock_id = (select stock_id from stock where ticker = ?)");
			ps.setInt(1, user_id);
			ps.setString(2, ticker);
			
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int os_id = rs.getInt("os_id");
				int local_user_id = rs.getInt("os_id");
				int stock_id = rs.getInt("os_id");
				int local_quantity = rs.getInt("os_id");
				float purchase_price = rs.getFloat("os_id");
				String timestamp = rs.getString("os_id");
				results.add(new OwnedStock(os_id, local_user_id, stock_id, local_quantity, purchase_price, timestamp));
			}
			
			
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
		
		return results;
	}
}
