package elements;

public class SellingOrder extends Order implements Comparable<SellingOrder>{

	/**
	 * Constructor for a SellingOrder object
	 * @param traderID ID of the trader who gave selling order
	 * @param amount The amount of PQoins
	 * @param price The price that the trader wants to sell from 
	 */
	public SellingOrder(int traderID, double amount, double price) {
		super(traderID, amount, price);
	}

	@Override
	public int compareTo(SellingOrder e) {
		if(this.price != e.price) {
			if(this.price>e.price){
				return 1;
			}
			else {
				return -1;
			}
		}		
		else {
			if(this.amount!=e.amount) {
				if(this.amount>e.amount) {
					return -1;
				}
				else {
					return 1;
				}
			}
			else {
				return this.traderID - e.traderID;
			}
		}
	}

	

}
