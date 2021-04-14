package napatel_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Search() {
		super();
		System.out.println("in constructor");
	}

	public void init(ServletConfig config) throws ServletException {
		System.out.println("in init");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Helloowowowow");
		String query = request.getParameter("query");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		out.println("{\"resText\": \"hello morl\"}");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
