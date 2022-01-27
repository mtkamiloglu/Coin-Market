package elements;

public class Wallet {
	private double dollars;
	private double coins;
	private double blockedDollars;
	private double blockedCoins;

	/**
	 * Constructor for a wallet object
	 * @param dollars
	 * @param coins
	 */
	public Wallet(double dollars, double coins) {
		this.dollars = dollars;
		this.coins = coins;
	}
	/**
	 * Returns dollars of the owner of this wallet
	 * @return amount of dollar in this wallet
	 */
	public double getDollars() {
		return this.dollars;
	}
	/**
	 * Returns coins of the owner of this wallet
	 * @return amount of coin in this wallet
	 */
	public double getCoins() {
		return this.coins;
	}
	/**
	 * Returns blocked dollars of the owner of this wallet
	 * @return amount of blocked dollars in this wallet
	 */
	public double getBlockedDollars() {
		return this.blockedDollars;
	}
	/**
	 * Returns blocked coins of the owner of this wallet
	 * @return amount of blocked coin in this wallet
	 */
	public double getBlockedCoins() {
		return this.blockedCoins;
	}
	
	/**
	 * Method for changing the amount of dollars in the wallet
	 * @param reserved
	 */
	public void setDollars(double reserved) {
		this.dollars += reserved;
	}
	
	/**
	 * Method for changing the amount of PQoins in the wallet
	 * @param reserved
	 */
	public void setCoins(double reserved) {
		this.coins += reserved;
	}
	
	/**
	 * Method for changing the amount of blocked dollars in the wallet
	 * @param reserved
	 */
	public void setBlockedDollars(double reserved) {
		this.blockedDollars += reserved;
	}
	
	/**
	 * Method for changing the amount of blocked PQoins in the wallet
	 * @param reserved
	 */
	public void setBlockedCoins(double reserved) {
		this.blockedCoins += reserved;
	}
}
