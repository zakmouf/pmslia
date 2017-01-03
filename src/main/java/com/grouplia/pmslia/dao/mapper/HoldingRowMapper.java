package com.grouplia.pmslia.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.grouplia.pmslia.domain.Holding;
import com.grouplia.pmslia.domain.Stock;

public class HoldingRowMapper extends BaseRowMapper implements RowMapper<Holding> {

	@Override
	public Holding mapRow(ResultSet rs, int rowNum) throws SQLException {
		Holding holding = mapHolding(rs, "holding");
		Stock stock = mapStock(rs, "stock");
		holding.setStock(stock);
		return holding;
	}

}
