package com.grouplia.pmslia.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.grouplia.pmslia.domain.Holding;
import com.grouplia.pmslia.domain.Portfolio;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

public abstract class BaseRowMapper {

	protected Stock mapStock(ResultSet rs, String prefix) throws SQLException {
		Stock stock = new Stock();
		stock.setId(getLong(rs, prefix + "_id"));
		stock.setTicker(getString(rs, prefix + "_ticker"));
		stock.setName(getString(rs, prefix + "_name"));
		stock.setDateCount(getInteger(rs, prefix + "_date_count"));
		stock.setFirstDate(getDate(rs, prefix + "_date_first"));
		stock.setLastDate(getDate(rs, prefix + "_date_last"));
		return stock;
	}

	protected Price mapPrice(ResultSet rs, String prefix) throws SQLException {
		Price price = new Price();
		price.setDate(getDate(rs, prefix + "_date"));
		price.setValue(getDouble(rs, prefix + "_value"));
		return price;
	}

	protected Portfolio mapPortfolio(ResultSet rs, String prefix) throws SQLException {
		Portfolio portfolio = new Portfolio();
		portfolio.setId(getLong(rs, prefix + "_id"));
		portfolio.setName(getString(rs, prefix + "_name"));
		portfolio.setStartDate(getDate(rs, prefix + "_date_start"));
		return portfolio;
	}

	protected Holding mapHolding(ResultSet rs, String prefix) throws SQLException {
		Holding holding = new Holding();
		holding.setQuantity(getDouble(rs, prefix + "_quantity"));
		return holding;
	}

	protected Long getLong(ResultSet rs, String columnLabel) throws SQLException {
		long l = rs.getLong(columnLabel);
		return rs.wasNull() ? null : Long.valueOf(l);
	}

	protected Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
		int i = rs.getInt(columnLabel);
		return rs.wasNull() ? null : Integer.valueOf(i);
	}

	protected String getString(ResultSet rs, String columnLabel) throws SQLException {
		String s = rs.getString(columnLabel);
		return rs.wasNull() ? null : s;
	}

	protected Date getDate(ResultSet rs, String columnLabel) throws SQLException {
		java.sql.Date d = rs.getDate(columnLabel);
		return rs.wasNull() ? null : new Date(d.getTime());
	}

	protected Double getDouble(ResultSet rs, String columnLabel) throws SQLException {
		double d = rs.getDouble(columnLabel);
		return rs.wasNull() ? null : Double.valueOf(d);
	}

}
