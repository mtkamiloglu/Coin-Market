package elements;


public class Order{

	double amount;
	double price;
	int traderID;
	
	/**
	 * Constructor for a order object
	 * @param traderID traderID ID of the trader who gave order
	 * @param amount amount The amount of PQoins
	 * @param price price The price that the trader wants to sell/buy from
	 */
	public Order(int traderID, double amount, double price) {
		this.traderID = traderID;
		this.amount = amount;
		this.price = price;
	}
	
	/**
	 * Getter method for the amount of order
	 * @return amount
	 */
	public double getAmount() {
		return this.amount;
	}
	
	/**
	 * Getter method for the price of order
	 * @return price
	 */
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * Getter method for the trader who gave order
	 * @return traderID
	 */
	public int getTraderId() {
		return this.traderID;
	}
}

