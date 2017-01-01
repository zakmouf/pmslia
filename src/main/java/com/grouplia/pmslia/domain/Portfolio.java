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
		return msg("id={0,number,0},name={1},startDate={2,date,yyyy-MM-dd},indice=[{3}]", id, name, startDate, indice);
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
