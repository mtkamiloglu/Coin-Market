package elements;

public class Trader {
	private int id;
	private Wallet wallet;
	public static int numberOfUsers = 0;
	
	/**
	 * Constructor for a trader object
	 * @param dollars The initial amount of dollars in trader's wallet
	 * @param coins The initial amount of PQoins in trader's wallet
	 */
	public Trader(double dollars, double coins) {
		this.wallet = new Wallet(dollars, coins);
		this.id = numberOfUsers;
		numberOfUsers++;
	}

	/**
	 * This method sells given amount of PQoins
	 * @param amount
	 * @param price
	 * @param market
	 * @return
	 */
	
	public int sell(double amount, double price, Market market) {
		
		if(this.wallet.getCoins() >= amount) {
			SellingOrder sellOrder = new SellingOrder(this.id, amount, price);
			market.giveSellOrder(sellOrder);
			this.wallet.setBlockedCoins(amount);
			this.wallet.setCoins(-amount);
		}
		else {
			market.numberOfInvalidQueries++;
		}
		return 0;
	}

	
	/**
	 * This method buys given amount of PQoins from market
	 * @param amount
	 * @param price
	 * @param market
	 * @return
	 */
	
	public int buy(double amount, double price, Market market) {
		double total = amount*price;
		
		if(this.wallet.getDollars() >= total) {
			BuyingOrder buyOrder = new BuyingOrder(this.id, amount, price);
			market.giveBuyOrder(buyOrder);
			this.wallet.setBlockedDollars(total);
			this.wallet.setDollars(-total);
		}
		else {
			market.numberOfInvalidQueries++;
		}
		return 0;
	}

	/**
	 * Getter method for trader's ID
	 * @return id
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Getter method for trader's wallet
	 * @return wallet
	 */
	public Wallet getWallet() {
		return this.wallet;
	}
	
}
