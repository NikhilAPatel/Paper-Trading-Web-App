package napatel_CSCI201L_Assignment4;

import static utils.Constants.tiingo_token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Stock;
import models.StockMeta;

//General todos
//TODO change db username and password to root before submit

@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Search() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String query = request.getParameter("query");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		try {
			out.println(getStockDetails(query));
		} catch (Exception e) {
			out.println("{\"error\": true, \"errorMessage\":\"Please check your internet connection and try again\"}");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public static String getStockDetails(String ticker) throws IOException {
		URL url = new URL("https://api.tiingo.com/iex/" + ticker + "?token=" + tiingo_token);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");

		// Case when ticker does not exist
		if (con.getResponseCode() == 404) {
			System.out.println(ticker + " 218937892does not exist. All involved trades will be purged.");
			return "{\"error\": \"true\", \"errorMessage\":\"Ticker " + ticker.toUpperCase() + " does not exist\"}";
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
				return getAllStockDetails(stock, response.toString());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(ticker + " 2837219873does not exist. All involved trades will be purged.");
				return "{\"error\": \"true\", \"errorMessage\":\"Ticker " + ticker.toUpperCase() + " does not exist\"}";
			}
		}
	}

	public static String getAllStockDetails(Stock stock, String dailyDetails) throws IOException {
		URL url = new URL("https://api.tiingo.com/tiingo/daily/" + stock.getTicker() + "?token=" + tiingo_token);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		StockMeta stockmeta;
		Gson gson = new GsonBuilder().create();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");

		// Case when ticker does not exist
		if (con.getResponseCode() == 404) {
			System.out.println(stock.getTicker() + " 128937928173does not exist. All involved trades will be purged.");
			return "{\"error\": \"true\", \"errorMessage\":\"Ticker " + stock.getTicker().toUpperCase()
					+ " does not exist\"}";
		} else {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				stockmeta = gson.fromJson(String.valueOf(response).replace("[", "").replace("]", ""), StockMeta.class);
			} catch (Exception e) {
				System.out
						.println(stock.getTicker() + "  2987389217does not exist. All involved trades will be purged.");
				return "{\"error\": \"true\", \"errorMessage\":\"Ticker " + stock.getTicker().toUpperCase()
						+ " does not exist\"}";
			}
		}

		stock.absorbMeta(stockmeta);
		stock.generateChange();
		stock.correctMid();
		stock.roundVals();
		stock.formatTimestamp();
		stock.setMarketOpen();
		stock.addToDb();
		return gson.toJson(stock);
	}
}
