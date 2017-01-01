package com.grouplia.pmslia.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.grouplia.pmslia.dao.PriceDao;
import com.grouplia.pmslia.dao.mapper.PriceRowMapper;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

@Repository("priceDao")
public class PriceDaoImpl extends BaseDaoImpl implements PriceDao {

	private String selectColumn = "p.f_date as price_date, p.f_value as price_value";
	private String findSql = "select " + selectColumn + " from t_price p where p.stock_id = ?";
	private String insertSql = "insert into t_price (stock_id, f_date, f_value) values (?, ?, ?)";
	private String deleteSql = "delete from t_price where stock_id = ?";
	private String deletePriceSql = "delete from t_price where stock_id = ? and f_date = ?";

	@Override
	public List<Price> find(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		List<Price> prices = queryForList(findSql, args, argTypes, new PriceRowMapper());
		Collections.sort(prices);
		return prices;
	}

	@Override
	public void insert(Stock stock, List<Price> prices) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(prices);
		for (Price price : prices) {
			insert(stock, price);
		}
	}

	@Override
	public void insert(Stock stock, Price price) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(price);
		Assert.notNull(price.getDate());
		Assert.notNull(price.getValue());
		Object[] args = { stock.getId(), price.getDate(), price.getValue() };
		int[] argTypes = { Types.NUMERIC, Types.DATE, Types.NUMERIC };
		insert(insertSql, args, argTypes);
	}

	@Override
	public void delete(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		delete(deleteSql, args, argTypes);
	}

	@Override
	public void delete(Stock stock, Price price) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(price);
		Assert.notNull(price.getDate());
		Object[] args = { stock.getId(), price.getDate() };
		int[] argTypes = { Types.NUMERIC, Types.DATE };
		delete(deletePriceSql, args, argTypes);
	}

	@Override
	public List<Price> findFrom(Stock stock, Date fromDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		List<Price> prices = find(stock);
		List<Price> filterPrices = new ArrayList<Price>();
		for (Price price : prices) {
			if (price.getDate().compareTo(fromDate) >= 0) {
				filterPrices.add(price);
			}
		}
		return filterPrices;
	}

	@Override
	public List<Price> findFromInclusive(Stock stock, Date fromDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		List<Price> prices = find(stock);
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
	public List<Price> findBetween(Stock stock, Date fromDate, Date toDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		Assert.notNull(toDate);
		Assert.isTrue(fromDate.compareTo(toDate) <= 0);
		List<Price> prices = find(stock);
		List<Price> filterPrices = new ArrayList<Price>();
		for (Price price : prices) {
			if (price.getDate().compareTo(fromDate) >= 0 && price.getDate().compareTo(toDate) <= 0) {
				filterPrices.add(price);
			}
		}
		return filterPrices;
	}

	@Override
	public List<Price> findBetweenInclusive(Stock stock, Date fromDate, Date toDate) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Assert.notNull(fromDate);
		Assert.notNull(toDate);
		Assert.isTrue(fromDate.compareTo(toDate) <= 0);
		List<Price> prices = find(stock);
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

}
