package utils;

import static utils.Constants.dbAddress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import models.OwnedStock;

public final class Utils {

	public static final float round(float num) {
		final DecimalFormat f = new DecimalFormat("##.00");
		return Float.parseFloat(f.format(num));
	}
	
	public static final float getStockPrice(String ticker) {
		//TODO
		return 0;
	}
	
	//takes in a boolean value to modify return value: true for Ask and false for Bid
	public static final float getCurrentSpread(boolean buy) {
		//TODO
		float ask = 0;
		float bid = 0;
		
		
		if(buy) {
			return ask;
		}else {
			return bid;
		}
	}
	
	public static final float getUserBalance(int user_id) {
		//TODO
		
		return 0;
	}
	
	public static final boolean marketClosed() {
		//TODO
		
		return false;
	}
	
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
