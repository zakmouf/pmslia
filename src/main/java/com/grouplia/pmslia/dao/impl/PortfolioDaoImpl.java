package com.grouplia.pmslia.dao.impl;

import java.sql.Types;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.grouplia.pmslia.dao.PortfolioDao;
import com.grouplia.pmslia.dao.mapper.HoldingRowMapper;
import com.grouplia.pmslia.dao.mapper.PortfolioRowMapper;
import com.grouplia.pmslia.domain.Holding;
import com.grouplia.pmslia.domain.Portfolio;

@Repository("profolioDao")
public class PortfolioDaoImpl extends BaseDaoImpl implements PortfolioDao {

	private String selectPortfoliosSql = "select p.* from v_portfolio p";
	private String selectPortfolioByIdSql = "select p.* from v_portfolio p where p.portfolio_id = ?";

	private String insertPortfolioSql = "insert into t_portfolio (f_id, f_name, f_date_start, indice_id) values (?, ?, ?, ?)";
	private String updatePortfolioSql = "update t_portfolio set f_name = ?, f_date_start = ?, indice_id = ? where f_id = ?";
	private String deletePortfolioSql = "delete from t_portfolio where f_id = ?";

	private String selectHoldingSql = "select h.* from v_holding h where h.portfolio_id = ?";

	private String insertHoldingSql = "insert into t_holding (portfolio_id, f_quantity, stock_id) values (?, ?, ?)";
	private String deleteHoldingSql = "delete from t_holding where portfolio_id = ? and stock_id = ?";
	private String deleteHoldingsSql = "delete from t_holding where portfolio_id = ?";

	@Override
	public List<Portfolio> findPortfolios() {
		Object[] args = {};
		int[] argTypes = {};
		List<Portfolio> portfolios = queryForList(selectPortfoliosSql, args, argTypes, new PortfolioRowMapper());
		Collections.sort(portfolios);
		logger.debug(msg("find portfolios : [{0,number,0}]", portfolios.size()));
		return portfolios;
	}

	@Override
	public Portfolio findPortfolioById(Long id) {
		Assert.notNull(id);
		Object[] args = { id };
		int[] argTypes = { Types.NUMERIC };
		Portfolio portfolio = queryForObject(selectPortfolioByIdSql, args, argTypes, new PortfolioRowMapper());
		logger.debug(msg("find portfolio by id=[{0,number,0}] : portfolio=[{1}]", id, portfolio));
		return portfolio;
	}

	@Override
	public void insertPortfolio(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.isNull(portfolio.getId());
		Assert.notNull(portfolio.getName());
		Assert.notNull(portfolio.getStartDate());
		Assert.notNull(portfolio.getIndice());
		Assert.notNull(portfolio.getIndice().getId());
		portfolio.setId(getNextId());
		Object[] args = { portfolio.getId(), portfolio.getName(), portfolio.getStartDate(),
				portfolio.getIndice().getId() };
		int[] argTypes = { Types.NUMERIC, Types.VARCHAR, Types.DATE, Types.NUMERIC };
		int insert = insert(insertPortfolioSql, args, argTypes);
		logger.debug(msg("insert portfolio=[{0}] : [{1,number,0}]", portfolio, insert));
	}

	@Override
	public void updatePortfolio(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Assert.notNull(portfolio.getName());
		Assert.notNull(portfolio.getStartDate());
		Assert.notNull(portfolio.getIndice());
		Assert.notNull(portfolio.getIndice().getId());
		Object[] args = { portfolio.getName(), portfolio.getStartDate(), portfolio.getIndice().getId(),
				portfolio.getId() };
		int[] argTypes = { Types.VARCHAR, Types.DATE, Types.NUMERIC, Types.NUMERIC };
		int update = update(updatePortfolioSql, args, argTypes);
		logger.debug(msg("update portfolio=[{0}] : [{1,number,0}]", portfolio, update));
	}

	@Override
	public void deletePortfolio(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Object[] args = { portfolio.getId() };
		int[] argTypes = { Types.NUMERIC };
		int delete = delete(deletePortfolioSql, args, argTypes);
		logger.debug(msg("delete portfolio=[{0}] : [{1,number,0}]", portfolio, delete));
	}

	@Override
	public List<Holding> findHoldings(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Object[] args = { portfolio.getId() };
		int[] argTypes = { Types.NUMERIC };
		List<Holding> holdings = queryForList(selectHoldingSql, args, argTypes, new HoldingRowMapper());
		Collections.sort(holdings);
		logger.debug(msg("find holdings for portfolio=[{0}] : [{1,number,0}]", portfolio, holdings.size()));
		return holdings;
	}

	@Override
	public void insertHolding(Portfolio portfolio, Holding holding) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Assert.notNull(holding);
		Assert.notNull(holding.getQuantity());
		Assert.notNull(holding.getStock());
		Assert.notNull(holding.getStock().getId());
		Object[] args = { portfolio.getId(), holding.getQuantity(), holding.getStock().getId() };
		int[] argTypes = { Types.NUMERIC, Types.NUMERIC, Types.NUMERIC };
		int insert = insert(insertHoldingSql, args, argTypes);
		logger.debug(msg("insert holding=[{0}] for portfolio=[{1}] : [{2,number,0}]", holding, portfolio, insert));
	}

	@Override
	public void insertHoldings(Portfolio portfolio, List<Holding> holdings) {
		Assert.notNull(holdings);
		for (Holding holding : holdings) {
			insertHolding(portfolio, holding);
		}
	}

	@Override
	public void deleteHolding(Portfolio portfolio, Holding holding) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Assert.notNull(holding);
		Assert.notNull(holding.getStock());
		Assert.notNull(holding.getStock().getId());
		Object[] args = { portfolio.getId(), holding.getStock().getId() };
		int[] argTypes = { Types.NUMERIC, Types.NUMERIC };
		int delete = delete(deleteHoldingSql, args, argTypes);
		logger.debug(msg("delete holding=[{0}] for portfolio=[{1}] : [{2,number,0}]", holding, portfolio, delete));
	}

	@Override
	public void deleteHoldings(Portfolio portfolio) {
		Assert.notNull(portfolio);
		Assert.notNull(portfolio.getId());
		Object[] args = { portfolio.getId() };
		int[] argTypes = { Types.NUMERIC };
		int delete = delete(deleteHoldingsSql, args, argTypes);
		logger.debug(msg("delete holdings for portfolio=[{0}] : [{1,number,0}]", portfolio, delete));
	}

}
