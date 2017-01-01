package com.grouplia.pmslia.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.grouplia.pmslia.dao.PriceDao;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;
import com.grouplia.pmslia.service.UpdateService;

@Service("updateService")
public class UpdateServiceImpl extends BaseServiceImpl implements UpdateService {

	@Value("${update.increment}")
	private Integer increment;

	@Value("#{new java.text.SimpleDateFormat('${update.start.date.pattern}').parse('${update.start.date}')}")
	private Date startDate;

	@Value("${update.url.pattern}")
	private String loadUrlPattern;

	@Resource
	private PriceDao priceDao;

	@Override
	@Transactional
	public void update(List<Stock> stocks) {
		Date toDate = DateUtils.truncate(new Date(), Calendar.DATE);
		toDate = DateUtils.addDays(toDate, -1);
		for (Stock stock : stocks) {
			Date fromDate = startDate;
			List<Price> prices = priceDao.find(stock);
			if (!prices.isEmpty()) {
				Price price = prices.get(prices.size() - 1);
				Date lastDate = price.getDate();
				fromDate = DateUtils.addDays(lastDate, 1);
			}
			int update = updateInternal(stock, fromDate, toDate);
			logger.info(
					msg("update [{0,number,0}] prices for stock [{1}] from [{2,date,yyyy-MM-dd}] to [{3,date,yyyy-MM-dd}]",
							update, stock.getTicker(), fromDate, toDate));
		}
	}

	private int updateInternal(Stock stock, Date fromDate, Date toDate) {
		logger.debug(
				msg("update increment [{0,number,0}] for stock [{1}] from [{2,date,yyyy-MM-dd}] to [{3,date,yyyy-MM-dd}]",
						increment, stock.getTicker(), fromDate, toDate));
		int update = 0;
		Calendar calendar = Calendar.getInstance();
		Date fromDateIncr = fromDate;
		calendar.setTime(fromDateIncr);
		while (fromDateIncr.compareTo(toDate) <= 0) {
			calendar.add(Calendar.DATE, increment);
			Date toDateIncr = calendar.getTime();
			if (toDateIncr.compareTo(toDate) > 0) {
				toDateIncr = toDate;
			}
			List<Price> prices = loadInternal(stock, fromDateIncr, toDateIncr);
			update += prices.size();
			priceDao.insert(stock, prices);
			logger.debug(
					msg("update internal [{0,number,0}/{1,number,0}] prices from [{2,date,yyyy-MM-dd}] to [{3,date,yyyy-MM-dd}]",
							prices.size(), update, fromDateIncr, toDateIncr));
			calendar.add(Calendar.DATE, 1);
			fromDateIncr = calendar.getTime();
		}
		return update;
	}

	private List<Price> loadInternal(Stock stock, Date fromDate, Date toDate) {

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
