package elements;

public class Transaction {
	private SellingOrder sellingOrder;
	private BuyingOrder buyingOrder;
	
	/**
	 * Constructor for a transactions object
	 * @param sellOrder selling order that will be executed 
	 * @param buyOrder buying order that will be executed 
	 */
	public Transaction(SellingOrder sellOrder, BuyingOrder buyOrder) {
		this.sellingOrder = sellOrder;
		this.buyingOrder = buyOrder;
	}
}
