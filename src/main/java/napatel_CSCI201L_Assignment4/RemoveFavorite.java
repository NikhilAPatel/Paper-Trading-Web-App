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

import static utils.Constants.dbAddress;

@WebServlet("/RemoveFavorite")
public class RemoveFavorite extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RemoveFavorite() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String ticker = request.getParameter("ticker");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		int rs;
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			ps = conn.prepareStatement("delete from Favorite where user_id=? and stock_id= (select stock_id from Stock where ticker=?);");
			ps.setInt(1,  user_id);
			ps.setString(2,  ticker);
			
			rs = ps.executeUpdate();
		} catch (SQLException | ClassNotFoundException sqle) {
			sqle.printStackTrace();
			System.out.println("oaisjfdias"+sqle.getMessage());
			out.println("{\"removed\":\"false\"}");
			System.out.println("printed");
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				// TODO handle
				System.out.println("26798376728"+sqle.getMessage());
				out.println("{\"removed\":\"false\"}");
			}
		}
		
		out.println("{\"removed\":\"true\"}");
		System.out.println("{\"removed\":\"true\"}");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}