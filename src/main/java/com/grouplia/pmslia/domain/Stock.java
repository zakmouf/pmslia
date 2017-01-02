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
		buf.append(msg("id={0,number,0}", id));
		buf.append(msg(",ticker={0}", ticker));
		buf.append(msg(",name={0}", name));
		buf.append(msg(",dateCount={0}", dateCount));
		buf.append(msg(",firstDate={0,date,yyyy-MM-dd}", firstDate));
		buf.append(msg(",lastDate={0,date,yyyy-MM-dd}", lastDate));
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
