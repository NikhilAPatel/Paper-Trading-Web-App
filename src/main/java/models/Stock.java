package models;

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
	private float mid;
	private float bidSize;
	private float askSize;
	private float prevClose;
	private float percentChange;
	private float change;
	private float last;
	
	
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
	public float getMid() {
		return mid;
	}
	public void setMid(float mid) {
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
	@Override
	public String toString() {
		return "Stock [high=" + high + ", low=" + low + ", open=" + open + ", close=" + close + ", volume=" + volume
				+ ", name=" + name + ", description=" + description + ", ticker=" + ticker + ", exchangeCode="
				+ exchangeCode + ", startDate=" + startDate + ", date=" + date + ", timestamp=" + timestamp
				+ ", bidPrice=" + bidPrice + ", askPrice=" + askPrice + ", mid=" + mid + ", bidSize=" + bidSize
				+ ", askSize=" + askSize + ", prevClose=" + prevClose + ", percentChange=" + percentChange + ", change="
				+ change + ", last=" + last + "]";
	}
	
	
	
	
	
	
	
	
	
	
	

}
