package com.grouplia.pmslia.domain;

import java.util.Date;

public class Stock extends BaseEntity implements Comparable<Stock> {

	private static final long serialVersionUID = 1L;

	private String ticker;
	private String name;
	private Integer dateCount;
	private Date firstDate;
	private Date lastDate;

	public Stock() {

	}

	public Stock(String ticker) {
		setTicker(ticker);
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

	public Integer getDateCount() {
		return dateCount;
	}

	public void setDateCount(Integer dateCount) {
		this.dateCount = dateCount;
	}

	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(msg("id=%1$d", id));
		buf.append(msg(",ticker=%1$s", ticker));
		buf.append(msg(",name=%1$s", name));
		buf.append(msg(",dateCount=%1$d", dateCount));
		buf.append(msg(",firstDate=%1$tF", firstDate));
		buf.append(msg(",lastDate=%1$tF", lastDate));
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
