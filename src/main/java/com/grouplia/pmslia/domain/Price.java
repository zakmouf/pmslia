package com.grouplia.pmslia.domain;

import java.util.Date;

public class Price extends BaseObject implements Comparable<Price> {

	private static final long serialVersionUID = 1L;

	private Date date;
	private Double value;

	public Price() {

	}

	public Price(Date date) {
		setDate(date);
	}

	public Price(Date date, Double value) {
		setDate(date);
		setValue(value);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return msg("date={0,date,yyyy-MM-dd},value={1,number,0.00}", date, value);
	}

	@Override
	public boolean equals(Object other) {
		return date.equals(((Price) other).getDate());
	}

	@Override
	public int compareTo(Price other) {
		return date.compareTo(other.getDate());
	}

	@Override
	public int hashCode() {
		return date.hashCode();
	}

}
