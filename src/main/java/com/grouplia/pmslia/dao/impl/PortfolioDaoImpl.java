package com.grouplia.pmslia.dao.impl;

import java.sql.Types;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.grouplia.pmslia.dao.PortfolioDao;
import com.grouplia.pmslia.dao.mapper.PortfolioRowMapper;
import com.grouplia.pmslia.domain.Portfolio;

@Repository("profolioDao")
public class PortfolioDaoImpl extends BaseDaoImpl implements PortfolioDao {

	private String selectColumn = "p.f_id as portfolio_id, p.f_name as portfolio_name, p.f_date_start as portfolio_date_start, "
			+ "i.f_id as indice_id, i.f_ticker as indice_ticker, i.f_name as indice_name";
	private String selectAllQuery = "select " + selectColumn
			+ " from t_portfolio p join t_stock i on i.f_id = p.indice_id";
	private String selectByIdQuery = "select " + selectColumn
			+ " from t_portfolio p join t_stock i on i.f_id = p.indice_id where p.f_id = ?";
	private String insertQuery = "insert into t_portfolio (f_id, f_name, f_date_start, indice_id) values (?, ?, ?, ?)";
	private String updateQuery = "update t_portfolio set f_name = ?, f_date_start = ?, indice_id = ? where f_id = ?";
	private String deleteQuery = "delete from t_portfolio where f_id = ?";
	// private String selectHoldingQuery = "select h.* from v_holding h where
	// h.portfolio_id = ?";
	// private String insertHoldingQuery = "insert into t_holding (portfolio_id,
	// f_quantity, stock_id) values (?, ?, ?)";
	// private String deleteHoldingQuery = "delete from t_holding where
	// portfolio_id = ?";

	@Override
	public List<Portfolio> findAll() {
		Object[] args = {};
		int[] argTypes = {};
		List<Portfolio> portfolios = queryForList(selectAllQuery, args, argTypes, new PortfolioRowMapper());
		Collections.sort(portfolios);
		logger.debug(msg("find [{0,number,0}] portfolios", portfolios.size()));
		return portfolios;
	}

	@Override
	public Portfolio findById(Long id) {
		Assert.notNull(id);
		Object[] args = { id };
		int[] argTypes = { Types.NUMERIC };
		Portfolio portfolio = queryForObject(selectByIdQuery, args, argTypes, new PortfolioRowMapper());
		logger.debug(msg("find by id=[{0,number,0}] portfolio=[{1}]", id, portfolio));
		// if (portfolio != null) {
		// List<Holding> holdings = findHoldings(portfolio);
		// portfolio.getHoldings().addAll(holdings);
		// }
		return portfolio;

	}

	@Override
	public void insert(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.isNull(portfolio.getId());
		Assert.notNull(portfolio.getName());
		Assert.notNull(portfolio.getStartDate());
		Assert.notNull(portfolio.getIndice());
		Assert.notNull(portfolio.getIndice().getId());
		// Assert.notNull(portfolio.getHoldings());
		// Assert.notEmpty(portfolio.getHoldings());
		portfolio.setId(getNextId());
		Object[] args = { portfolio.getId(), portfolio.getName(), portfolio.getStartDate(),
				portfolio.getIndice().getId() };
		int[] argTypes = { Types.NUMERIC, Types.VARCHAR, Types.DATE, Types.NUMERIC };
		insert(insertQuery, args, argTypes);
		logger.debug(msg("insert portfolio=[{0}]", portfolio));
		// for (Holding holding : portfolio.getHoldings()) {
		// insertHolding(portfolio, holding);
		// }
	}

	@Override
	public void update(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Assert.notNull(portfolio.getName());
		Assert.notNull(portfolio.getStartDate());
		Assert.notNull(portfolio.getIndice());
		Assert.notNull(portfolio.getIndice().getId());
		// Assert.notNull(portfolio.getHoldings());
		// Assert.notEmpty(portfolio.getHoldings());
		// deleteHoldings(portfolio);
		Object[] args = { portfolio.getName(), portfolio.getStartDate(), portfolio.getIndice().getId(),
				portfolio.getId() };
		int[] argTypes = { Types.VARCHAR, Types.DATE, Types.NUMERIC, Types.NUMERIC };
		update(updateQuery, args, argTypes);
		logger.debug(msg("update portfolio=[{0}]", portfolio));
		// for (Holding holding : portfolio.getHoldings()) {
		// insertHolding(portfolio, holding);
		// }
	}

	@Override
	public void delete(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Object[] args = { portfolio.getId() };
		int[] argTypes = { Types.NUMERIC };
		delete(deleteQuery, args, argTypes);
		logger.debug(msg("delete portfolio=[{0}]", portfolio));
	}

	// private List<Holding> findHoldings(Portfolio portfolio) {
	// Assert.notNull(portfolio);
	// Assert.notNull(portfolio.getId());
	// Object[] args = { portfolio.getId() };
	// int[] argTypes = { Types.NUMERIC };
	// List<Holding> holdings = queryForList(selectHoldingQuery, args, argTypes,
	// new HoldingRowMapper());
	// Collections.sort(holdings);
	// logger.debug(msg("find [{0,number,0}] holdings for portfolio=[{1}]",
	// holdings.size(), portfolio));
	// return holdings;
	// }

	// private void insertHolding(Portfolio portfolio, Holding holding) {
	// Assert.notNull(portfolio);
	// Assert.notNull(portfolio.getId());
	// Assert.notNull(holding);
	// Assert.notNull(holding.getQuantity());
	// Assert.notNull(holding.getStock());
	// Assert.notNull(holding.getStock().getId());
	// Object[] args = { portfolio.getId(), holding.getQuantity(),
	// holding.getStock().getId() };
	// int[] argTypes = { Types.NUMERIC, Types.NUMERIC, Types.NUMERIC };
	// insert(insertHoldingQuery, args, argTypes);
	// logger.debug(msg("insert holding=[{0}] in portfolio=[{1}]", holding,
	// portfolio));
	// }

	// private void deleteHoldings(Portfolio portfolio) {
	// Assert.notNull(portfolio);
	// Assert.notNull(portfolio.getId());
	// Object[] args = { portfolio.getId() };
	// int[] argTypes = { Types.NUMERIC };
	// delete(deleteHoldingQuery, args, argTypes);
	// logger.debug(msg("delete holdings from portfolio=[{0}]", portfolio));
	// }

}
