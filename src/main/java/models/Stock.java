package models;

import static utils.Utils.round;
import static utils.Constants.dbAddress;
import static utils.Utils.marketClosed;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


public class Stock {
	private float high;
	private float low;
	private float open;
	private float close;
	private float volume;
	private String name;
	private String description;
	private String ticker;
	private String exchangeCode;
	private String startDate;
	private String date;
	private String timestamp;
	private float bidPrice;
	private float askPrice;
	private String mid;
	private float bidSize;
	private float askSize;
	private float prevClose;
	private float percentChange;
	private float change;
	private float last;
	private boolean marketOpen;
	private String quoteTimestamp;
	private String lastSaleTimestamp;
	private String formattedTimestamp;
	
	
	
	public boolean isMarketOpen() {
		return marketOpen;
	}
	public void setMarketOpen(boolean marketOpen) {
		this.marketOpen = marketOpen;
	}
	public float getLast() {
		return last;
	}
	public void setLast(float last) {
		this.last = last;
	}
	public float getChange() {
		return change;
	}
	public void generateChange() {
		this.change=this.last-this.prevClose;
		this.generatePercentChange();
	}
	public float getPercentChange() {
		return percentChange;
	}
	private void generatePercentChange() {
		this.percentChange = (this.change*100)/this.prevClose;
	}
	public float getPrevClose() {
		return prevClose;
	}
	public void setPrevClose(float prevClose) {
		this.prevClose = prevClose;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public float getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(float bidPrice) {
		this.bidPrice = bidPrice;
	}
	public float getAskPrice() {
		return askPrice;
	}
	public void setAskPrice(float askPrice) {
		this.askPrice = askPrice;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public float getBidSize() {
		return bidSize;
	}
	public void setBidSize(float bidSize) {
		this.bidSize = bidSize;
	}
	public float getAskSize() {
		return askSize;
	}
	public void setAskSize(float askSize) {
		this.askSize = askSize;
	}
	public float getHigh() {
		return high;
	}
	public void setHigh(float high) {
		this.high = high;
	}
	public float getLow() {
		return low;
	}
	public void setLow(float low) {
		this.low = low;
	}
	public float getOpen() {
		return open;
	}
	public void setOpen(float open) {
		this.open = open;
	}
	public float getClose() {
		return close;
	}
	public void setClose(float close) {
		this.close = close;
	}
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getExchangeCode() {
		return exchangeCode;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public void absorbMeta(StockMeta meta) {
		this.name=meta.getName();
		this.description=meta.getDescription();
		this.ticker=meta.getTicker();
		this.exchangeCode=meta.getExchangeCode();
		this.startDate=meta.getStartDate();
	}
	
	public void correctMid() {
		if (this.mid == "0" || this.mid==""|| this.mid==null) {
			this.mid="-";
		}
	}
	
	public void roundVals() {
		this.roundLast();
		this.roundPercentChange();
		this.roundChange();
	}
	
	public void roundLast() {
		this.last=round(this.last);
	}
	
	public void roundPercentChange() {
		this.percentChange=round(this.percentChange);
	}
	
	public void roundChange() {
		this.change=round(this.change);
	}
	
	public void formatTimestamp() {
		this.formattedTimestamp=this.timestamp.substring(0, this.timestamp.indexOf("T")+9).replace("T", " ");
	}
	
	public void setMarketOpen() throws IOException {	
		this.marketOpen=!marketClosed();
	}
	
	public void addToDb() {
		//Initialization
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbAddress);

			//Add the stock to the stock table if it isn't already there
			ps = conn.prepareStatement("insert ignore into Stock (ticker) values (\""+this.ticker.toUpperCase()+"\")");
			rs = ps.executeUpdate();
			
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
	
	@Override
	public String toString() {
		return "Stock [high=" + high + ", low=" + low + ", open=" + open + ", close=" + close + ", volume=" + volume
				+ ", name=" + name + ", description=" + description + ", ticker=" + ticker + ", exchangeCode="
				+ exchangeCode + ", startDate=" + startDate + ", date=" + date + ", timestamp=" + timestamp
				+ ", bidPrice=" + bidPrice + ", askPrice=" + askPrice + ", mid=" + mid + ", bidSize=" + bidSize
				+ ", askSize=" + askSize + ", prevClose=" + prevClose + ", percentChange=" + percentChange + ", change="
				+ change + ", last=" + last + ", marketOpen=" + marketOpen + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
