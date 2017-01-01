package com.grouplia.pmslia.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.grouplia.pmslia.domain.Portfolio;

public class PortfolioRowMapper extends BaseRowMapper implements RowMapper<Portfolio> {

	@Override
	public Portfolio mapRow(ResultSet rs, int rowNum) throws SQLException {
		Portfolio portfolio = mapPortfolio(rs);
		portfolio.setIndice(mapStock(rs, "indice"));
		return portfolio;
	}

}
