package napatel_CSCI201L_Assignment4;

import static utils.Constants.dbAddress;
import static utils.Utils.getOwnedStock;
import static utils.Utils.getStockAttribute;
import static utils.Utils.getTimestamp;
import static utils.Utils.getUserBalance;
import static utils.Utils.marketClosed;
import static utils.Utils.round;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.OwnedStock;

@WebServlet("/sell")
public class Sell extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Sell() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
	}

	// Receives userId, ticker, and quantity and buys the stock
	// Either returns {success: boolean, message: String}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		// Parameter Acquisition
		String ticker = request.getParameter("ticker");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));

		// Check if the quantity is invalid
		if (quantity < 1) {
			out.println("{\"success\": false, \"message\": \"FAILED: Purchase not possible\"}");
			return;
		}

		// Return error if the market is closed
		if (marketClosed()) {
			out.println("{\"success\": false, \"message\": \"FAILED: Market is closed\"}");
			return;
		}

		// Do sell
		float bid = (float) getStockAttribute(ticker, "bid");
		if (bid == -1) {
			out.println("{\"success\": false, \"message\": \"FAILED\"}");
		} else if (bid == 0) {
			bid = (float) getStockAttribute(ticker, "last");
		}
		out.println(sell(user_id, ticker, bid, quantity));

	}

	// Returns JSON string that doGet will return to client
	public String sell(int user_id, String ticker, float bid, int quantity) {
		// Initialization
		float balance = getUserBalance(user_id);
		final float original_quantity = quantity;

		// get all of this stock that this user owns
		ArrayList<OwnedStock> ownedStock = getOwnedStock(user_id, ticker);

		// Make sure that the user owns enough stock to do the sale
		if (ownedStock.size() == 0) {
			return "{\"success\": false, \"message\": \"FAILED: Purchase not possible\"}";
		}
		int totalStockUnits = 0;
		for (OwnedStock s : ownedStock) {
			totalStockUnits += s.getQuantity();
		}
		if (totalStockUnits < quantity) {
			return "{\"success\": false, \"message\": \"FAILED: Cannot sell more stock than you own\"}";
		}

		// Calculate the user's new balance
		float newBalance = balance + (bid * quantity);

		int entries_to_sell = 0;
		int overflow = 0;

		for (OwnedStock s : ownedStock) {
			if (quantity == 0) {
				break;
			}
			if (quantity >= s.getQuantity()) {
				// Sell all of it and decrease quantity
				quantity -= s.getQuantity();
				boolean success = sellStock(s.getOs_id());
				if (!success) {
					return "{\"success\": false, \"message\": \"FAILED\"}";
				}
			} else {
				// Sell part of it and end
				boolean success = sellPartialStock(s.getOs_id(), s.getQuantity() - quantity);
				if (!success) {
					return "{\"success\": false, \"message\": \"FAILED\"}";
				}
			}
		}

		// Update the balance
		this.updateBalance(user_id, newBalance);

		// If the code is here, we probably succeeded
		return "{\"success\": true, \"message\": \"SUCCESS: Executed purchase of " + original_quantity + " shares of "
				+ ticker + " for $" + round(bid) + "\"}";
	}

	public static boolean sellStock(int os_id) {
		// Initialization
		Connection conn = null;
		PreparedStatement ps = null;
		int rs;

		// Do the actual sale
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			ps = conn.prepareStatement("delete from Owned_Stock where os_id=?");
			ps.setInt(1, os_id);

			rs = ps.executeUpdate();

			if (rs == 0) {// It probably failed
				return false;
			}

			// Return success message
			return true;

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
		return false;
	}

	public void updateBalance(int user_id, float newBalance) {
		// Initialization
		Connection conn = null;
		PreparedStatement ps = null;
		int rs;
		String timestamp = getTimestamp();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			// Update the user's balance
			ps = conn.prepareStatement("update User set balance = ? where user_id=?");
			ps.setFloat(1, newBalance);
			ps.setInt(2, user_id);

			rs = ps.executeUpdate();

			return;

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
	}

	public static boolean sellPartialStock(int os_id, int quantity) {
		// Initialization
		Connection conn = null;
		PreparedStatement ps = null;
		int rs;

		// Do the actual sale
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			ps = conn.prepareStatement("update Owned_Stock set quantity=? where os_id=?");
			ps.setInt(1, quantity);
			ps.setInt(2, os_id);

			rs = ps.executeUpdate();

			System.out.println(rs);

			if (rs == 0) {// It probably failed
				return false;
			}

			// Return success message
			return true;

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
		return false;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}