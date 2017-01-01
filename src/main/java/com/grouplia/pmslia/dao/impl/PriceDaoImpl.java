package com.grouplia.pmslia.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.grouplia.pmslia.dao.PriceDao;
import com.grouplia.pmslia.dao.mapper.PriceRowMapper;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

@Repository("priceDao")
public class PriceDaoImpl extends BaseDaoImpl implements PriceDao {

	private String findLastDateSql = "select max (f_date) from t_price where stock_id = ?";
	private String findSql = "select p.* from t_price p where p.stock_id = ?";
	private String insertSql = "insert into t_price (stock_id, f_date, f_value) values (?, ?, ?)";
	private String deleteSql = "delete from t_price where stock_id = ?";
	private String deletePriceSql = "delete from t_price where stock_id = ? and f_date = ?";

	@Override
	public Date findLastDate(Stock stock) {
		Assert.notNull(stock);
		Assert.notNull(stock.getId());
		Object[] args = { stock.getId() };
		int[] argTypes = { Types.NUMERIC };
		Date lastDate = queryForObject(findLastDateSql, args, argTypes, Date.class);
		return lastDate;
	}

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

	@Value("${price.load.url.pattern}")
	private String loadUrlPattern;

	@Override
	public List<Price> loadBetween(Stock stock, Date fromDate, Date toDate) {

		Assert.notNull(stock);
		Assert.notNull(stock.getTicker());
		Assert.notNull(fromDate);
		Assert.notNull(toDate);
		Assert.isTrue(fromDate.compareTo(toDate) <= 0);

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(toDate);
		String url = msg(loadUrlPattern, stock.getTicker(), fromCalendar.get(Calendar.MONTH),
				fromCalendar.get(Calendar.DATE), fromCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
				toCalendar.get(Calendar.DATE), toCalendar.get(Calendar.YEAR));

		InputStream input = null;
		List<String> lines = new ArrayList<String>();
		try {
			input = new URL(url).openStream();
			InputStreamReader inputReader = new InputStreamReader(input);
			BufferedReader buffReader = new BufferedReader(inputReader);
			String line = buffReader.readLine();
			while (line != null) {
				lines.add(line);
				line = buffReader.readLine();
			}
			lines = lines.subList(1, lines.size());
		} catch (IOException ex) {
			logger.warn(msg("failed to open url=[{0}]", url));
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				// ignore
			}
		}

		List<Price> prices = new ArrayList<Price>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (String line : lines) {

			String[] tokens = StringUtils.delimitedListToStringArray(line, ",");

			Price price = new Price();

			String dateAsString = tokens[0];
			try {
				price.setDate(dateFormat.parse(dateAsString));
			} catch (ParseException ex) {
				throw new IllegalArgumentException(msg("failed to parse date [{0}]", dateAsString), ex);
			}

			String valueAsString = tokens[6];
			try {
				price.setValue(Double.valueOf(valueAsString));
			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException(msg("failed to parse double [{0}]", valueAsString), ex);
			}

			prices.add(price);

		}

		Collections.sort(prices);

		logger.debug(msg("load [{0}] prices from=[{1,date,yyyy-MM-dd}] to=[{2,date,yyyy-MM-dd}] for stock=[{3}]",
				prices.size(), fromDate, toDate, stock));

		return prices;

	}

}
