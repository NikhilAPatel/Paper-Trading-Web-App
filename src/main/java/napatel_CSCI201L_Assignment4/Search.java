package napatel_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

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
		out.println(getStockDetails(query));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static String getStockDetails(String ticker) throws IOException{
        URL url = new URL("https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?token=55db81be5a6b3afad4ecb01acfc13c73204c0ad0");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        //Case when ticker does not exist
        if (con.getResponseCode() == 404) {
            System.out.println(ticker + "72163792167936912 does not exist. All involved trades will be purged.");
            return "{\"error\": \"true\", \"errorMessage\":\"Ticker "+ticker.toUpperCase()+" does not exist\"}";
        } else {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            } catch (Exception e) {
            	e.printStackTrace();
                System.out.println(ticker + " does not exist. All involved trades will be purged.");
                return "{\"error\": \"true\", \"errorMessage\":\"Ticker "+ticker.toUpperCase()+" does not exist\"}";
            }
        }
    }
}
