package com.grouplia.pmslia.domain;

public class Holding extends BaseObject implements Comparable<Holding> {

	private static final long serialVersionUID = 1L;

	private Double quantity;
	private Stock stock;

	public Holding() {

	}

	public Holding(Double quantity, Stock stock) {
		setQuantity(quantity);
		setStock(stock);
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(msg("quantity=%1$.2f", quantity));
		buf.append(msg(",stock=[%1$s]", stock));
		return buf.toString();
	}

	@Override
	public boolean equals(Object other) {
		return quantity.equals(((Holding) other).getQuantity()) && stock.equals(((Holding) other).getStock());
	}

	@Override
	public int compareTo(Holding other) {
		return stock.compareTo(other.getStock());
	}

	@Override
	public int hashCode() {
		return stock.hashCode();
	}

}
