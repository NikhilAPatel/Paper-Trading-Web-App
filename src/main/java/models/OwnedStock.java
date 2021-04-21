package models;

//TODO also make a timestamp that is a Date object or something
//then make this class implement comparable so i can sort in purchase order
//then i decide which stocks i need to sell

public class OwnedStock implements Comparable<OwnedStock>{
	private int os_id;
	private int user_id;
	private int stock_id;
	private int quantity;
	private float purchase_price;
	private String timestamp;
	private long timestampMillis;

	public OwnedStock(int os_id, int user_id, int stock_id, int quantity, float purchase_price, String timestamp) {
		super();
		this.os_id = os_id;
		this.user_id = user_id;
		this.stock_id = stock_id;
		this.quantity = quantity;
		this.purchase_price = purchase_price;
		this.timestamp = timestamp;
		this.timestampMillis = Long.parseLong(this.timestamp);
	}
	public int getOs_id() {
		return os_id;
	}
	public void setOs_id(int os_id) {
		this.os_id = os_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getStock_id() {
		return stock_id;
	}
	public void setStock_id(int stock_id) {
		this.stock_id = stock_id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getPurchase_price() {
		return purchase_price;
	}
	public void setPurchase_price(float purchase_price) {
		this.purchase_price = purchase_price;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getTimestampMillis() {
		return timestampMillis;
	}
	public void setTimestampMillis(long timestampMillis) {
		this.timestampMillis = timestampMillis;
	}
	
	
	@Override
	public String toString() {
		return "OwnedStock [os_id=" + os_id + ", user_id=" + user_id + ", stock_id=" + stock_id + ", quantity="
				+ quantity + ", purchase_price=" + purchase_price + ", timestamp=" + timestamp + ", timestampMillis="
				+ timestampMillis + "]";
	}
	@Override
	public int compareTo(OwnedStock o) {
		if(this.timestampMillis==o.getTimestampMillis()) {
			return 0;
		}else if(this.timestampMillis>o.getTimestampMillis()) {
			return 1;
		}else {
			return -1;
		}
	}
	
	

}
