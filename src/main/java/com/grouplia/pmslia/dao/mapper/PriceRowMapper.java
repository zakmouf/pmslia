package com.grouplia.pmslia.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.grouplia.pmslia.domain.Price;

public class PriceRowMapper extends BaseRowMapper implements RowMapper<Price> {

	@Override
	public Price mapRow(ResultSet rs, int rowNum) throws SQLException {
		Price price = new Price();
		price.setDate(getDate(rs, "f_date"));
		price.setValue(getDouble(rs, "f_value"));
		return price;
	}

}
