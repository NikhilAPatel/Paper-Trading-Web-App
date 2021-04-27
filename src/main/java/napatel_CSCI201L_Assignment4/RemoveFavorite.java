package napatel_CSCI201L_Assignment4;

import static utils.Constants.dbAddress;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

			ps = conn.prepareStatement(
					"delete from Favorite where user_id=? and stock_id= (select stock_id from Stock where ticker=?);");
			ps.setInt(1, user_id);
			ps.setString(2, ticker);

			rs = ps.executeUpdate();
		} catch (SQLException | ClassNotFoundException sqle) {
			sqle.printStackTrace();
			System.out.println("oaisjfdias" + sqle.getMessage());
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
				System.out.println("26798376728" + sqle.getMessage());
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