package elements;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Market {
	private int fee;
	private PriorityQueue<SellingOrder> sellingOrders;
	private PriorityQueue<BuyingOrder> buyingOrders;
	private ArrayList<Transaction> transactions;
	private double buyingCurrentPrice;
	private double sellingCurrentPrice;
	private double averageCurrentPrice;
	public int numberOfInvalidQueries;

	/**
	 * Constructor for market object
	 * @param fee market fee
	 */
	public Market(int fee) {
		this.fee = fee;
		this.sellingOrders = new PriorityQueue<SellingOrder>();
		this.buyingOrders = new PriorityQueue<BuyingOrder>();
		this.transactions = new ArrayList<Transaction>();
	}

	/**
	 * This method adds given selling order to sellingOrders priority queue
	 * @param order The buying order to be added to the queue
	 */
	public void giveSellOrder(SellingOrder order) {
		sellingOrders.add(order);
	}

	/**
	 * This method adds given buying order to buyingOrders priority queue
	 * @param order The buying order to be added to the queue
	 */
	public void giveBuyOrder(BuyingOrder order) {
		buyingOrders.add(order);
	}

	/**
	 * This method makes an open market operation
	 * @param price The desired price
	 * @param traders ArrayList of traders
	 */
	public void makeOpenMarketOperation(double price, ArrayList<Trader> traders) {
		if(buyingOrders.peek()==null && sellingOrders.peek()==null) {
			return;
		}
		else if(buyingOrders.peek()==null && price < sellingOrders.peek().price) {
			return;
		}
		else if(sellingOrders.peek()==null && price > buyingOrders.peek().price) {
			return;
		}
		else if(price <= buyingOrders.peek().price) {
			while(price <= buyingOrders.peek().price) {
				traders.get(0).getWallet().setCoins(buyingOrders.peek().amount);
				SellingOrder adjustment = new SellingOrder(0, buyingOrders.peek().amount, buyingOrders.peek().price);
				giveSellOrder(adjustment);
				checkTransactions(traders);
				if(buyingOrders.peek()==null) {
					return;
				}
			}
		}
		else if(price >= sellingOrders.peek().price ) {
			while(price >= sellingOrders.peek().price) {
				traders.get(0).getWallet().setDollars(buyingOrders.peek().amount * buyingOrders.peek().price);
				BuyingOrder adjustment = new BuyingOrder(0 , sellingOrders.peek().amount, sellingOrders.peek().price);
				giveBuyOrder(adjustment);
				checkTransactions(traders);
				if(sellingOrders.peek()==null) {
					break;
				}
			}

		}
	}

	/**
	 * The method that checks if there is a possible transaction
	 * @param
	 */
	public void checkTransactions(ArrayList<Trader> traders) {
		if(this.buyingOrders.peek()==null || this.sellingOrders.peek() ==null) {
			//			System.out.println("No transaction");
			return;
		}

		else if(this.buyingOrders.peek().price >= this.sellingOrders.peek().price) {				//Orderlar arasýnda örtüþme var

			if(buyingOrders.peek().amount==sellingOrders.peek().amount) {  					// Alýþ emriyle satýþ emrinin miktarlarý ayný olursa
				BuyingOrder buy = buyingOrders.poll();
				SellingOrder sell = sellingOrders.poll();
				transactions.add(new Transaction(sell, buy));
				double fPrice = sell.price;
				double fAmount = sell.amount;
				double total;

				if(buy.price > sell.price) {
					total = fPrice * fAmount;
					double priceDifference = (buy.price * buy.amount) - total;

					//Alýcý
					traders.get(buy.traderID).getWallet().setBlockedDollars(-(buy.price * buy.amount));
					traders.get(buy.traderID).getWallet().setDollars(priceDifference);
					traders.get(buy.traderID).getWallet().setCoins(buy.amount);
					//Satýcý
					traders.get(sell.traderID).getWallet().setBlockedCoins(-sell.amount);
					traders.get(sell.traderID).getWallet().setDollars(total * (1- (double) fee/1000));
				}
				else {
					total = fPrice * fAmount;

					//Alýcý
					traders.get(buy.traderID).getWallet().setBlockedDollars(-total);
					traders.get(buy.traderID).getWallet().setCoins(buy.amount);
					//Satýcý
					traders.get(sell.traderID).getWallet().setBlockedCoins(-sell.amount);
					traders.get(sell.traderID).getWallet().setDollars(total * (1- (double) fee/1000));
				}
				checkTransactions(traders);
			}

			else if(buyingOrders.peek().amount > sellingOrders.peek().amount) {				//Alýþ emrinin miktarý daha fazla olursa
				BuyingOrder buy = buyingOrders.poll();
				SellingOrder sell = sellingOrders.poll();
				double fPrice = sell.price;
				double fAmount = sell.amount;
				double total = fPrice * fAmount;

				//Kalan kýsým için yeni order oluþturma
				BuyingOrder remainBuy = new BuyingOrder(buy.traderID, buy.amount - sell.amount, buy.price);
				this.giveBuyOrder(remainBuy);	

				//Ýþleme giren kýsým için yeni order oluþturma
				BuyingOrder processedBuy = new BuyingOrder(buy.traderID, sell.amount, sell.price);

				transactions.add(new Transaction(sell, processedBuy));

				if(buy.price > sell.price) {
					double priceDifference = (buy.price * fAmount) - total; 

					//Alýcý 
					traders.get(buy.traderID).getWallet().setBlockedDollars(-(buy.price * fAmount));
					traders.get(buy.traderID).getWallet().setDollars(priceDifference);
					traders.get(buy.traderID).getWallet().setCoins(sell.amount);
					//Satýcý
					traders.get(sell.traderID).getWallet().setBlockedCoins(-sell.amount);
					traders.get(sell.traderID).getWallet().setDollars(total * (1- (double) fee/1000));
				}

				else {
					//Alýcý 
					traders.get(buy.traderID).getWallet().setBlockedDollars(-total);
					traders.get(buy.traderID).getWallet().setCoins(sell.amount);
					//Satýcý
					traders.get(sell.traderID).getWallet().setBlockedCoins(-sell.amount);
					traders.get(sell.traderID).getWallet().setDollars(total * (1- (double) fee/1000));
				}
				checkTransactions(traders);
			}

			else {																			//Satýþ emrinin miktarý daha fazla olursa
				BuyingOrder buy = buyingOrders.poll();
				SellingOrder sell = sellingOrders.poll();
				double fPrice = sell.price;
				double fAmount = buy.amount;
				double total = fPrice * fAmount;

				//Kalan kýsým için yeni order oluþturma
				SellingOrder remainSell = new SellingOrder(sell.traderID, sell.amount - buy.amount, sell.price);
				this.giveSellOrder(remainSell);

				//Ýþleme giren kýsým için yeni order oluþturma
				SellingOrder processedSell= new SellingOrder(sell.traderID, buy.amount, sell.price);

				transactions.add(new Transaction(processedSell, buy));

				if(buy.price > sell.price) {
					double priceDifference = (buy.price * fAmount) - total;

					//Alýcý 
					traders.get(buy.traderID).getWallet().setBlockedDollars(-(buy.price * fAmount));
					traders.get(buy.traderID).getWallet().setDollars(priceDifference);
					traders.get(buy.traderID).getWallet().setCoins(buy.amount);
					//Satýcý
					traders.get(sell.traderID).getWallet().setBlockedCoins(-buy.amount);
					traders.get(sell.traderID).getWallet().setDollars(total * (1- (double) fee/1000));
				}
				else {
					//Alýcý
					traders.get(buy.traderID).getWallet().setBlockedDollars(-total);
					traders.get(buy.traderID).getWallet().setCoins(buy.amount);
					//Satýcý
					traders.get(sell.traderID).getWallet().setBlockedCoins(-buy.amount);
					traders.get(sell.traderID).getWallet().setDollars(total * (1- (double) fee/1000));
				}
				checkTransactions(traders);
			}
		}

		else {
			return;
		}

	}

	/**
	 * The method that returns buying current price
	 * @return buyingCurrentPrice
	 */
	public double getBuyingCurrentPrice() {
		if(sellingOrders.peek()==null) {
			return 0;
		}
		else {
			buyingCurrentPrice = sellingOrders.peek().price;
			return buyingCurrentPrice;
		}
	}
	/**
	 * The method that returns selling current price
	 * @return sellingCurrentPrice
	 */
	public double getSellingCurrentPrice() {
		if(buyingOrders.peek()==null) {
			return 0;
		}
		else {
			sellingCurrentPrice = buyingOrders.peek().price;
			return sellingCurrentPrice;
		}
	}

	/**
	 * The method that returns average current price
	 * @return averageCurrentPrice
	 */
	public double getPrintingCurrentPrice() {
		if(buyingOrders.peek()==null && sellingOrders.peek()==null) {
			return 0;
		}
		else if(buyingOrders.peek()!=null && sellingOrders.peek()==null) {
			return buyingOrders.peek().price;
		}
		else if(buyingOrders.peek()==null && sellingOrders.peek()!=null) {
			return sellingOrders.peek().price;
		}
		else {
			averageCurrentPrice = (getBuyingCurrentPrice() + getSellingCurrentPrice()) / 2;
			return averageCurrentPrice;
		}
	}

	/**
	 * The method that returns sellingOrders priority queue
	 * @return sellingOrders
	 */
	public PriorityQueue<SellingOrder> getSellingOrders(){
		return this.sellingOrders;
	}

	/**
	 * The method that returns buyingOrders priority queue
	 * @return buyingOrders
	 */
	public PriorityQueue<BuyingOrder> getBuyingOrders(){
		return this.buyingOrders;
	}

	/**
	 * The method that returns transactions ArrayList
	 * @return transactions
	 */
	public ArrayList<Transaction> getTransactions() {
		return this.transactions;
	}
}
