package models;

import java.io.IOException;
import java.util.ArrayList;
import static utils.Utils.getStock;
import static utils.Utils.round;

public class PortfolioStock {
	private String ticker;
	private String name;
	private int quantity;
	private float avgcost;
	private float totalcost;
	private float change;
	private float currentPrice;
	private float marketValue;
	
	public PortfolioStock(String ticker, String name, ArrayList<OwnedStock> ownedStock) throws IOException {
		//Get stuff we know right away
		this.ticker = ticker;
		Stock stock = getStock(ticker);
		this.name = name;
		this.currentPrice=stock.getLast();
		
		//Initialize variables we will calculate
		this.quantity=0;
		this.avgcost=0;
		
		//Calculate variables
		for(OwnedStock s: ownedStock) {
			this.avgcost=((this.avgcost*this.quantity)+s.getPurchase_price()*s.getQuantity())/(this.quantity+s.getQuantity());
			this.quantity+=s.getQuantity();
		}
		
		//Calculate remaining variables
		this.totalcost=this.avgcost*this.quantity;
		this.marketValue=this.quantity*this.currentPrice;
		this.change=(this.avgcost-stock.getLast())*-1;
		
		round(this.avgcost);
		round(this.totalcost);
		round(this.change);
		round(this.currentPrice);
		round(this.marketValue);
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getAvgcost() {
		return avgcost;
	}

	public void setAvgcost(float avgcost) {
		this.avgcost = avgcost;
	}

	public float getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(float totalcost) {
		this.totalcost = totalcost;
	}

	public float getChange() {
		return change;
	}

	public void setChange(float change) {
		this.change = change;
	}

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public float getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(float marketValue) {
		this.marketValue = marketValue;
	}

	@Override
	public String toString() {
		return "PortfolioStock [ticker=" + ticker + ", name=" + name + ", quantity=" + quantity + ", avgcost=" + avgcost
				+ ", totalcost=" + totalcost + ", change=" + change + ", currentPrice=" + currentPrice
				+ ", marketValue=" + marketValue + "]";
	}
	
	

}
