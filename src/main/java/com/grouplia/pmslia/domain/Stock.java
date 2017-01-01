package com.grouplia.pmslia.domain;

public class Stock extends BaseEntity implements Comparable<Stock> {

	private static final long serialVersionUID = 1L;

	private String ticker;
	private String name;

	public Stock() {

	}

	public Stock(String ticker) {
		setTicker(ticker);
	}

	public Stock(String ticker, String name) {
		setTicker(ticker);
		setName(name);
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(msg("id={0,number,0}", id));
		buf.append(msg(",ticker=\"{0}\"", ticker));
		buf.append(msg(",name=\"{0}\"", name));
		return buf.toString();
	}

	@Override
	public boolean equals(Object other) {
		return ticker.equals(((Stock) other).getTicker());
	}

	@Override
	public int compareTo(Stock other) {
		return ticker.compareTo(other.getTicker());
	}

	@Override
	public int hashCode() {
		return ticker.hashCode();
	}

}
