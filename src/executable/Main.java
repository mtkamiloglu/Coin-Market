package executable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import elements.SellingOrder;
import elements.BuyingOrder;
import elements.Market;
import elements.Order;
import elements.Trader;

public class Main{
	public static Random myRandom;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));

		int randomSeed = in.nextInt();
		int fee = in.nextInt();
		int noUsers = in.nextInt();
		int noQueries = in.nextInt();
		String empty = in.nextLine();

		Market market = new Market(fee);
		myRandom = new Random(randomSeed);

		ArrayList<Trader> traders = new ArrayList<Trader>();
		Order[] queries = new Order[noQueries];


		for(int i=0; i<noUsers; i++){
			String data = in.nextLine();
			String[] els = data.split(" ");
			double dollars = Double.parseDouble(els[0]);
			double pQoins = Double.parseDouble(els[1]);
			traders.add(new Trader(dollars, pQoins));
		}


		for(int m=0; m<noQueries; m++) {
			String data = in.nextLine();
			String[] els = data.split(" ");

//			System.out.println(data);

			//Buying order for specific price 													(Works well!)
			if(els[0].equals("10")) {
				int traderID = Integer.parseInt(els[1]);
				double price = Double.parseDouble(els[2]);
				double amount = Double.parseDouble(els[3]);
				traders.get(traderID).buy(amount, price, market);
			}

			//Buying order of market price														(Works well!)
			if(els[0].equals("11")) {
				int traderID = Integer.parseInt(els[1]);
				double amount = Double.parseDouble(els[2]);
				double price = market.getBuyingCurrentPrice();
				if(market.getSellingOrders().peek()!=null) {
					traders.get(traderID).buy(amount, price, market);
				}
				else {
					market.numberOfInvalidQueries++;
				}
			}

			//Selling order for specific price 													(Works well!)
			if(els[0].equals("20")) {
				int traderID = Integer.parseInt(els[1]);
				double price = Double.parseDouble(els[2]);
				double amount = Double.parseDouble(els[3]);
				traders.get(traderID).sell(amount, price, market);
			}

			//Give selling order for market price												(Works  well!)
			if(els[0].equals("21")) {
				int traderID = Integer.parseInt(els[1]);
				double amount = Double.parseDouble(els[2]);
				double price = market.getSellingCurrentPrice();
				if(market.getBuyingOrders().peek()!=null) {
					traders.get(traderID).sell(amount, price, market);
				}
				else {
					market.numberOfInvalidQueries++;
				}
			}

			//Deposit a certain amount of dollars to wallet										(Works well!)
			if(els[0].equals("3")) {
				int traderID = Integer.parseInt(els[1]);
				double deposit = Double.parseDouble(els[2]);
				traders.get(traderID).getWallet().setDollars(deposit);
			}

			//Withdraw a certain amount of dollars from wallet									(Works well!)
			if(els[0].equals("4")) {
				int traderID = Integer.parseInt(els[1]);
				double amount = Double.parseDouble(els[2]);
				if(traders.get(traderID).getWallet().getDollars() >= amount) {
					traders.get(traderID).getWallet().setDollars(-(amount));
				}
				else {
					market.numberOfInvalidQueries++;
				}
			}

			//Print wallet status																(Works well)															
			if(els[0].equals("5")) {
				int traderID = Integer.parseInt(els[1]);
				Trader trader = traders.get(traderID);
				out.println("Trader " + traderID + ": " + String.format("%.5f" ,trader.getWallet().getDollars()+trader.getWallet().getBlockedDollars()) + "$ " + String.format("%.5f" ,trader.getWallet().getCoins()+trader.getWallet().getBlockedCoins()) + "PQ");
			} 

			//Give rewards to all traders														(Works well!)
			if(els[0].equals("777")) {
				for(int i=0; i<traders.size(); i++) {
					traders.get(i).getWallet().setCoins(myRandom.nextDouble()*10);
				}
			}

			//Make an open market operation
			if(els[0].equals("666")) {
				int traderID = 0;
				double price = Double.parseDouble(els[1]);
				market.makeOpenMarketOperation(price, traders);
			}

			//Print the current market size														(Works well!)
			if(els[0].equals("500")) {
				SellingOrder[] sellOrders = market.getSellingOrders().toArray(new SellingOrder[market.getSellingOrders().size()]);
				BuyingOrder[] buyOrders = market.getBuyingOrders().toArray(new BuyingOrder[market.getBuyingOrders().size()]);
				double totalBQ = 0;
				double totalSQ = 0;

				for(SellingOrder e : sellOrders) {
					totalSQ += (e.getAmount());
				}
				for(BuyingOrder n : buyOrders) {
					totalBQ += (n.getAmount() * n.getPrice());
				}

				out.println("Current market size: " + String.format("%.5f", totalBQ) + " " + String.format("%.5f", totalSQ));
			}

			//Print number of successful transactions
			if(els[0].equals("501")) {
				out.println("Number of successful transactions: " + market.getTransactions().size());
			}

			//Print the number of invalid queries                               				(Works well)
			if(els[0].equals("502")) {
				out.println("Number of invalid queries: " + market.numberOfInvalidQueries);
			}

			//Print the current prices															(Works well)
			if(els[0].equals("505")) {
				double buyingPrice;
				double sellingPrice;

				if(market.getBuyingOrders().peek()!=null) {
					buyingPrice = market.getBuyingOrders().peek().getPrice();
				} else {
					buyingPrice = 0;
				}
				if(market.getSellingOrders().peek()!=null) {
					sellingPrice = market.getSellingOrders().peek().getPrice();
				} else {
					sellingPrice = 0;
				}

				out.println("Current prices: " + String.format("%.5f" , buyingPrice) +" "+ String.format("%.5f",sellingPrice) +" "+ String.format("%.5f", market.getPrintingCurrentPrice()));
			}

			//Print all traders' wallet status 													(Works well)
			if(els[0].equals("555")) {
				for(int i=0; i<traders.size(); i++) {
					Trader trader = traders.get(i);
					out.println("Trader " + i + ": " + String.format("%.5f" , trader.getWallet().getDollars() + trader.getWallet().getBlockedDollars()) + "$ " + String.format("%.5f" ,trader.getWallet().getCoins() + trader.getWallet().getBlockedCoins()) + "PQ");
				}
			}

			market.checkTransactions(traders);
		}	
		
//		System.out.println(market.getSellingOrders().peek().getPrice());
//		System.out.println(market.getBuyingOrders().size());
		
	}
}

