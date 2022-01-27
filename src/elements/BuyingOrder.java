package elements;

public class BuyingOrder extends Order implements Comparable<BuyingOrder>{

	/**
	 * Constructor for a BuyingOrder object
	 * @param traderID ID of the trader who gave buying order
	 * @param amount The amount of PQoins
	 * @param price The price that the trader wants to buy from 
	 */
	public BuyingOrder(int traderID, double amount, double price) {
		super(traderID, amount, price);
	}
	
	@Override
	public int compareTo(BuyingOrder e) {
		if(this.price != e.price) {
			if(this.price>e.price){
				return -1;
			}
			else {
				return 1;
			}
		}		
		else {
			if(this.amount!=e.amount) {
				if(this.amount >e.amount) {
					return -1;
				}
				else {
					return 1;
				}
			}
			else {
				if(this.traderID < e.traderID) {
					return -1;
				} else {
					return 1;
				}
			}
		}
	}

	
	
	

}
