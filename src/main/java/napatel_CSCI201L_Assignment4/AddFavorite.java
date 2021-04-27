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

@WebServlet("/AddFavorite")
public class AddFavorite extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddFavorite() {
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

			ps = conn.prepareStatement("insert ignore into Stock (ticker) values (?);");
			ps.setString(1, ticker);

			rs = ps.executeUpdate();

			System.out.print(user_id);

			ps2 = conn.prepareStatement(
					"insert ignore into Favorite (user_id, stock_id) values (?, (select stock_id from Stock where ticker=?));");
			ps2.setInt(1, user_id);
			ps2.setString(2, ticker);

			rs = ps2.executeUpdate();

		} catch (SQLException | ClassNotFoundException sqle) {
			sqle.printStackTrace();
			System.out.println("oaisjfdias" + sqle.getMessage());
			out.println("{\"success\":\"false\"}");
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
				out.println("{\"success\":\"false\"}");
			}
		}

		out.println("{\"success\":\"true\"}");
		System.out.println("{\"success\":\"true\"}");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}