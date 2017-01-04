package com.grouplia.pmslia.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.dao.mapper.PriceRowMapper;
import com.grouplia.pmslia.dao.mapper.StockRowMapper;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

@Repository("stockDao")
public class StockDaoImpl extends BaseDaoImpl implements StockDao {

	private String selectStocksSql = "select s.* from v_stock s";
	private String selectStockByIdSql = "select s.* from v_stock s where s.stock_id = ?";
	private String selectStockByTickerSql = "select s.* from v_stock s where s.stock_ticker = ?";

	private String insertStockSql = "insert into t_stock (f_id, f_ticker, f_name) values (?, ?, ?)";
	private String updateStockSql = "update t_stock set f_ticker = ?, f_name = ? where f_id = ?";
	private String deleteStockSql = "delete from t_stock where f_id = ?";

	private String selectParentQuery = "select s.* from v_stock s, t_stock_relation sr where sr.parent_id = s.stock_id and sr.child_id = ?";
	private String selectChildQuery = "select s.* from v_stock s, t_stock_relation sr where sr.child_id = s.stock_id and sr.parent_id = ?";
	private String insertRelationQuery = "insert into t_stock_relation (parent_id, child_id) values (?, ?)";
	private String deleteRelationQuery = "delete from t_stock_relation where parent_id = ? and child_id = ?";

	private String selectLastPriceSql = "select p.* from v_price p where p.stock_id = ? and p.price_date = (select max(p_.price_date) from v_price p_ where p_.stock_id = ?)";
	private String selectPriceSql = "select p.* from v_price p where p.stock_id = ?";

	private String insertPriceSql = "insert into t_price (stock_id, f_date, f_value) values (?, ?, ?)";
	private String deletePriceSql = "delete from t_price where stock_id = ? and f_date = ?";
	private String deletePricesSql = "delete from t_price where stock_id = ?";

	@Override
	public List<Stock> findStocks() {
		Object[] args = {};
		int[] argTypes = {};
		List<Stock> stocks = queryForList(selectStocksSql, args, argTypes, new StockRowMapper());
		Collections.sort(stocks);
		logger.debug(msg("find stocks : [%1$d]", stocks.size()));
		return stocks;
	}

	@Override
	public Stock findStockById(Long id) {
		Assert.notNull(id);
		Object[] args = { id };
		int[] argTypes = { Types.NUMERIC };
		Stock stock = queryForObject(selectStockByIdSql, args, argTypes, new StockRowMapper());
		logger.debug(msg("find stock by id=[%1$d] : stock=[%1$s]", id, stock));
		return stock;
	}

	@Override
	public Stock findStockByTicker(String ticker) {
		Assert.notNull(ticker);
		Object[] args = { ticker };
		int[] argTypes = { Types.VARCHAR };
		Stock stock = queryForObject(selectStockByTickerSql, args, argTypes, new StockRowMapper());
		logger.debug(msg("find stock by ticker=[%1$s] : stock=[%2$s]", ticker, stock));
		return stock;
	}

	@Override
	public void insertStock(Stock stock) {
		Assert.notNull(stock);
		Assert.isNull(stock.getId());
		Assert.notNull(stock.getTicker());
		stock.setId(getNextId());
		Object[] args = { stock.getId(), stock.getTicker(), stock.getName() };
		int[] argTypes = { Types.NUMERIC, Types.VARCHAR, Types.VARCHAR };
		int insert = insert(insertStockSql, args, argTypes);
		logger.debug(msg("insert stock=[%1$s] : [%2$d]", stock, insert));
	}

	@Override
	public void updateStock(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(stock.getTicker());
		Object[] args = { stock.getTicker(), stock.getName(), stock.getId() };
		int[] argTypes = { Types.VARCHAR, Types.VARCHAR, Types.NUMERIC };
		int update = update(updateStockSql, args, argTypes);
		logger.debug(msg("update stock=[%1$s] : [%2$d]", stock, update));
	}

	@Override
	public void deleteStock(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		int delete = delete(deleteStockSql, args, argTypes);
		logger.debug(msg("delete stock=[%1$s] : [%2$d]", stock, delete));
	}

	@Override
	public List<Stock> findParents(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		List<Stock> stocks = queryForList(selectParentQuery, args, argTypes, new StockRowMapper());
		Collections.sort(stocks);
		logger.debug(msg("find parents for stock=[%1$s] : [%2$d] ", stock, stocks.size()));
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
		logger.debug(msg("find children for stock=[%1$s] : [%2$d]", stock, stocks.size()));
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
		int insert = insert(insertRelationQuery, args, argTypes);
		logger.debug(msg("insert relation parent=[%1$s] child=[%2$s] : [%3$d]", parent, child, insert));
	}

	@Override
	public void deleteRelation(Stock parent, Stock child) {
		Assert.notNull(parent);
		Assert.notNull(parent.getId());
		Assert.notNull(child);
		Assert.notNull(child.getId());
		Object[] args = { parent.getId(), child.getId() };
		int[] argTypes = { Types.NUMERIC, Types.NUMERIC };
		int delete = delete(deleteRelationQuery, args, argTypes);
		logger.debug(msg("delete relation parent=[%1$s] child=[%2$s] : [%3$d]", parent, child, delete));
	}

	@Override
	public Price findLastPrice(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId(), stock.getId() };
		int[] argTypes = { Types.NUMERIC, Types.NUMERIC };
		Price price = queryForObject(selectLastPriceSql, args, argTypes, new PriceRowMapper());
		logger.debug(msg("find last price for stock=[%1$s] : price=[%2$s]", stock, price));
		return price;
	}

	@Override
	public List<Price> findPrices(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		List<Price> prices = queryForList(selectPriceSql, args, argTypes, new PriceRowMapper());
		Collections.sort(prices);
		logger.debug(msg("find prices for stock=[%1$s] : [%2$d]", stock, prices.size()));
		return prices;
	}

	@Override
	public List<Price> findPricesFrom(Stock stock, Date fromDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		List<Price> prices = findPrices(stock);
		List<Price> filterPrices = new ArrayList<Price>();
		for (Price price : prices) {
			if (price.getDate().compareTo(fromDate) >= 0) {
				filterPrices.add(price);
			}
		}
		return filterPrices;
	}

	@Override
	public List<Price> findPricesFromInclusive(Stock stock, Date fromDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		List<Price> prices = findPrices(stock);
		Date filterFromDate = null;
		for (Price price : prices) {
			if (price.getDate().compareTo(fromDate) <= 0) {
				filterFromDate = price.getDate();
			}
		}
		List<Price> filterPrices = new ArrayList<Price>();
		if (filterFromDate != null) {
			for (Price price : prices) {
				if (price.getDate().compareTo(filterFromDate) >= 0) {
					filterPrices.add(price);
				}
			}
		}
		return filterPrices;
	}

	@Override
	public List<Price> findPricesBetween(Stock stock, Date fromDate, Date toDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		Assert.notNull(toDate);
		Assert.isTrue(fromDate.compareTo(toDate) <= 0);
		List<Price> prices = findPrices(stock);
		List<Price> filterPrices = new ArrayList<Price>();
		for (Price price : prices) {
			if (price.getDate().compareTo(fromDate) >= 0 && price.getDate().compareTo(toDate) <= 0) {
				filterPrices.add(price);
			}
		}
		return filterPrices;
	}

	@Override
	public List<Price> findPricesBetweenInclusive(Stock stock, Date fromDate, Date toDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		Assert.notNull(toDate);
		Assert.isTrue(fromDate.compareTo(toDate) <= 0);
		List<Price> prices = findPrices(stock);
		Date filterFromDate = null;
		Date filterToDate = null;
		for (Price price : prices) {
			if (price.getDate().compareTo(fromDate) <= 0) {
				filterFromDate = price.getDate();
			}
			if (price.getDate().compareTo(toDate) >= 0) {
				if (filterToDate == null) {
					filterToDate = price.getDate();
				}
			}
		}
		List<Price> filterPrices = new ArrayList<Price>();
		if (filterFromDate != null && filterToDate != null) {
			for (Price price : prices) {
				if (price.getDate().compareTo(filterFromDate) >= 0 && price.getDate().compareTo(filterToDate) <= 0) {
					filterPrices.add(price);
				}
			}
		}
		return filterPrices;
	}

	@Override
	public void insertPrice(Stock stock, Price price) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(price);
		Assert.notNull(price.getDate());
		Assert.notNull(price.getValue());
		Object[] args = { stock.getId(), price.getDate(), price.getValue() };
		int[] argTypes = { Types.NUMERIC, Types.DATE, Types.NUMERIC };
		int insert = insert(insertPriceSql, args, argTypes);
		logger.debug(msg("insert price=[%1$s] for stock=[%2$s] : [%3$d]", price, stock, insert));
	}

	@Override
	public void insertPrices(Stock stock, List<Price> prices) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(prices);
		for (Price price : prices) {
			insertPrice(stock, price);
		}
	}

	@Override
	public void deletePrice(Stock stock, Price price) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(price);
		Assert.notNull(price.getDate());
		Object[] args = { stock.getId(), price.getDate() };
		int[] argTypes = { Types.NUMERIC, Types.DATE };
		int delete = delete(deletePriceSql, args, argTypes);
		logger.debug(msg("delete price=[%1$s] for stock=[%2$s] : [%3$d]", price, stock, delete));
	}

	@Override
	public void deletePrices(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		int delete = delete(deletePricesSql, args, argTypes);
		logger.debug(msg("delete prices for stock=[%1$s] : [%2$d]", stock, delete));
	}

}
