package com.grouplia.pmslia.dao.impl;

import java.sql.Types;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.dao.mapper.StockRowMapper;
import com.grouplia.pmslia.domain.Stock;

@Repository("stockDao")
public class StockDaoImpl extends BaseDaoImpl implements StockDao {

	private String selectColumn = "s.f_id as stock_id, s.f_ticker as stock_ticker, s.f_name as stock_name";
	private String selectAllQuery = "select " + selectColumn + " from t_stock s";
	private String selectByIdQuery = "select " + selectColumn + " from t_stock s where s.f_id = ?";
	private String selectByTickerQuery = "select " + selectColumn + " from t_stock s where s.f_ticker = ?";
	private String insertQuery = "insert into t_stock (f_id, f_ticker, f_name) values (?, ?, ?)";
	private String updateQuery = "update t_stock set f_ticker = ?, f_name = ? where f_id = ?";
	private String deleteQuery = "delete from t_stock where f_id = ?";
	private String selectParentQuery = "select " + selectColumn + " from t_stock s, t_stock_relation sr where sr.parent_id = s.f_id and sr.child_id = ?";
	private String selectChildQuery = "select " + selectColumn + " from t_stock s, t_stock_relation sr where sr.child_id = s.f_id and sr.parent_id = ?";
	private String insertRelationQuery = "insert into t_stock_relation (parent_id, child_id) values (?, ?)";
	private String deleteRelationQuery = "delete from t_stock_relation where parent_id = ? and child_id = ?";

	@Override
	public List<Stock> findAll() {
		Object[] args = {};
		int[] argTypes = {};
		List<Stock> stocks = queryForList(selectAllQuery, args, argTypes, new StockRowMapper());
		Collections.sort(stocks);
		logger.debug(msg("find [{0}] stocks", stocks.size()));
		return stocks;
	}

	@Override
	public Stock findById(Long id) {
		Assert.notNull(id);
		Object[] args = { id };
		int[] argTypes = { Types.NUMERIC };
		Stock stock = queryForObject(selectByIdQuery, args, argTypes, new StockRowMapper());
		logger.debug(msg("find by id=[{0}] stock=[{1}]", id, stock));
		return stock;
	}

	@Override
	public Stock findByTicker(String ticker) {
		Assert.notNull(ticker);
		Object[] args = { ticker };
		int[] argTypes = { Types.VARCHAR };
		Stock stock = queryForObject(selectByTickerQuery, args, argTypes, new StockRowMapper());
		logger.debug(msg("find by ticker=[{0}] stock=[{1}]", ticker, stock));
		return stock;
	}

	@Override
	public void insert(Stock stock) {
		Assert.notNull(stock);
		Assert.isNull(stock.getId());
		Assert.notNull(stock.getTicker());
		stock.setId(getNextId());
		Object[] args = { stock.getId(), stock.getTicker(), stock.getName() };
		int[] argTypes = { Types.NUMERIC, Types.VARCHAR, Types.VARCHAR };
		insert(insertQuery, args, argTypes);
		logger.debug(msg("insert stock=[{0}]", stock));
	}

	@Override
	public void update(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(stock.getTicker());
		Object[] args = { stock.getTicker(), stock.getName(), stock.getId() };
		int[] argTypes = { Types.VARCHAR, Types.VARCHAR, Types.NUMERIC };
		update(updateQuery, args, argTypes);
		logger.debug(msg("update stock=[{0}]", stock));
	}

	@Override
	public void delete(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		delete(deleteQuery, args, argTypes);
		logger.debug(msg("delete stock=[{0}]", stock));
	}

	@Override
	public List<Stock> findParents(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		List<Stock> stocks = queryForList(selectParentQuery, args, argTypes, new StockRowMapper());
		Collections.sort(stocks);
		logger.debug(msg("find [{0,number,0}] parents for stock=[{1}]", stocks.size(), stock));
		return stocks;
	}

	@Override
	public List<Stock> findChildren(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		List<Stock> stocks = queryForList(selectChildQuery, args, argTypes, new StockRowMapper());
		Collections.sort(stocks);
		logger.debug(msg("find [{0,number,0}] children for stock=[{1}]", stocks.size(), stock));
		return stocks;
	}

	@Override
	public void insertRelation(Stock parent, Stock child) {
		Assert.notNull(parent);
		Assert.notNull(parent.getId());
		Assert.notNull(child);
		Assert.notNull(child.getId());
		Object[] args = { parent.getId(), child.getId() };
		int[] argTypes = { Types.NUMERIC, Types.NUMERIC };
		insert(insertRelationQuery, args, argTypes);
		logger.debug(msg("insert relation parent=[{0}] child=[{1}]", parent, child));
	}

	@Override
	public void deleteRelation(Stock parent, Stock child) {
		Assert.notNull(parent);
		Assert.notNull(parent.getId());
		Assert.notNull(child);
		Assert.notNull(child.getId());
		Object[] args = { parent.getId(), child.getId() };
		int[] argTypes = { Types.NUMERIC, Types.NUMERIC };
		delete(deleteRelationQuery, args, argTypes);
		logger.debug(msg("delete relation parent=[{0}] child=[{1}]", parent, child));
	}

}
