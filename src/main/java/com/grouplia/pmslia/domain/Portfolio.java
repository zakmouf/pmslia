package com.grouplia.pmslia.domain;

import java.util.Date;

public class Portfolio extends BaseEntity implements Comparable<Portfolio> {

	private static final long serialVersionUID = 1L;

	private String name;
	private Date startDate;
	private Stock indice;

	public Portfolio() {

	}

	public Portfolio(String name, Date startDate, Stock indice) {
		this.name = name;
		this.startDate = startDate;
		this.indice = indice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Stock getIndice() {
		return indice;
	}

	public void setIndice(Stock indice) {
		this.indice = indice;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(msg("id=%1$d", id));
		buf.append(msg(",name=%1$s", name));
		buf.append(msg(",startDate=%1$tF", startDate));
		buf.append(msg(",indice=[%1$s]", indice));
		return buf.toString();
	}

	@Override
	public boolean equals(Object other) {
		return id.equals(((Portfolio) other).getId());
	}

	@Override
	public int compareTo(Portfolio other) {
		return id.compareTo(other.getId());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
