package napatel_CSCI201L_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import static utils.Constants.dbAddress;
import static utils.Utils.getStockAttribute;
import static utils.Utils.getUserBalance;
import static utils.Utils.marketClosed;
import static utils.Utils.getTimestamp;
import static utils.Utils.round;
import static utils.Utils.getAllOwnedStock;
import static napatel_CSCI201L_Assignment4.Search.getStockDetails;
import static utils.Utils.getStockAttributeFromId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.OwnedStock;
import models.PortfolioStock;
import models.Stock;
import models.StockMeta;

@WebServlet("/GetPortfolio")
public class GetPortfolio extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetPortfolio() {
		super();
	}
	

	public void init(ServletConfig config) throws ServletException {
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		ArrayList<PortfolioStock> portfolio = new ArrayList<>();
		HashMap<Integer, ArrayList<OwnedStock>> ownedStockGroups = new HashMap<>();
		
		//Parameter Acquisition
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		
		//Get the owned stock
		ArrayList<OwnedStock> ownedStock = getAllOwnedStock(user_id);
		
		//If the user owns no stock, return an error
		if(ownedStock.isEmpty()) {
			out.println("{\"error\":true}");
			return;
		}
		
		//Sort stock into hashmap
		for(OwnedStock s: ownedStock) {
			if(ownedStockGroups.containsKey(s.getStock_id())) {
				ownedStockGroups.get(s.getStock_id()).add(s);
			}else {
				ownedStockGroups.put(s.getStock_id(), new ArrayList<OwnedStock>());
				ownedStockGroups.get(s.getStock_id()).add(s);
			}
		}
		
		for(int i: ownedStockGroups.keySet()) {
			String ticker = getStockAttributeFromId(ownedStockGroups.get(i).get(0).getStock_id(), "ticker");
			String name = getStockAttributeFromId(ownedStockGroups.get(i).get(0).getStock_id(), "name");
			portfolio.add(new PortfolioStock(ticker, name, ownedStockGroups.get(i)));
		}

		System.out.println(portfolio);
		Gson gson = new Gson();
		out.println(gson.toJson(portfolio));
		
		
				
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}