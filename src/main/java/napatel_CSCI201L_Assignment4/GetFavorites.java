package napatel_CSCI201L_Assignment4;

import static napatel_CSCI201L_Assignment4.Search.getStockDetails;
import static utils.Constants.dbAddress;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetFavorites")
public class GetFavorites extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetFavorites() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs;
		ArrayList<String> tickerNames = new ArrayList<>();

		// Parameter Acquisition
		int user_id = Integer.parseInt(request.getParameter("user_id"));

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			ps = conn.prepareStatement(
					"select ticker from Stock where stock_id in (select stock_id from Favorite where user_id=?)");
			ps.setInt(1, user_id);

			rs = ps.executeQuery();

			while (rs.next()) {
				tickerNames.add(rs.getString("ticker"));
			}

			if (tickerNames.isEmpty()) {
				out.println("{\"error\": true}");
				return;
			}
			out.println("[");
			for (int i = 0; i < tickerNames.size(); i++) {
				out.println(getStockDetails(tickerNames.get(i)));
				if (i + 1 < tickerNames.size()) {
					out.println(",");
				}
			}
			out.println("]");
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}