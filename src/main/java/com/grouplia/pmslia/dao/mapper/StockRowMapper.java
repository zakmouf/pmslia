package com.grouplia.pmslia.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.grouplia.pmslia.domain.Stock;

public class StockRowMapper extends BaseRowMapper implements RowMapper<Stock> {

	@Override
	public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
		Stock stock = new Stock();
		stock.setId(getLong(rs, "f_id"));
		stock.setTicker(getString(rs, "f_ticker"));
		stock.setName(getString(rs, "f_name"));
		return stock;
	}

}
