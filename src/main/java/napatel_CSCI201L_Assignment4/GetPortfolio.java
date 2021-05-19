package napatel_CSCI201L_Assignment4;

import static utils.Utils.calculateStockValue;
import static utils.Utils.getAllOwnedStock;
import static utils.Utils.getStockAttributeFromId;
import static utils.Utils.getUserBalance;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import models.OwnedStock;
import models.PortfolioStock;

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

		// Initialization
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		ArrayList<PortfolioStock> portfolio = new ArrayList<>();
		HashMap<Integer, ArrayList<OwnedStock>> ownedStockGroups = new HashMap<>();

		// Parameter Acquisition
		int user_id = Integer.parseInt(request.getParameter("user_id"));

		// Get the owned stock
		ArrayList<OwnedStock> ownedStock = getAllOwnedStock(user_id);

		// If the user owns no stock, return an error
		if (ownedStock.isEmpty()) {
			float balance = getUserBalance(user_id);
			out.println("{\"error\":true, \"balance\":"+balance+", \"accountValue\":"+balance+"}");
			return;
		}

		// Sort stock into hashmap
		for (OwnedStock s : ownedStock) {
			if (ownedStockGroups.containsKey(s.getStock_id())) {
				ownedStockGroups.get(s.getStock_id()).add(s);
			} else {
				ownedStockGroups.put(s.getStock_id(), new ArrayList<OwnedStock>());
				ownedStockGroups.get(s.getStock_id()).add(s);
			}
		}

		for (int i : ownedStockGroups.keySet()) {
			String ticker = getStockAttributeFromId(ownedStockGroups.get(i).get(0).getStock_id(), "ticker");
			String name = getStockAttributeFromId(ownedStockGroups.get(i).get(0).getStock_id(), "name");
			portfolio.add(new PortfolioStock(ticker, name, ownedStockGroups.get(i)));
		}

		float cashBalance = getUserBalance(user_id);
		float totalAccountValue = cashBalance + calculateStockValue(portfolio);

		Gson gson = new Gson();

		String retString = "{\"stocks\":";
		retString += gson.toJson(portfolio);
		retString += ", \"balance\": " + cashBalance + ", \"accountValue\": " + totalAccountValue + "}";
		out.println(retString);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}