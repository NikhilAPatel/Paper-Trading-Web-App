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
				+ exchangeCode + ", startDate=" + startDate + "]";
	}
	
	

}
