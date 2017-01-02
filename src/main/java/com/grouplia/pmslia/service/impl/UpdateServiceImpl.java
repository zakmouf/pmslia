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
import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;
import com.grouplia.pmslia.service.UpdateService;

@Service("updateService")
public class UpdateServiceImpl extends BaseServiceImpl implements UpdateService {

	@Value("${update.name.url.pattern}")
	private String nameUrlPattern;

	@Value("${update.price.increment}")
	private Integer priceIncrement;

	@Value("#{new java.text.SimpleDateFormat('${update.price.start.date.pattern}').parse('${update.price.start.date}')}")
	private Date priceStartDate;

	@Value("${update.price.url.pattern}")
	private String priceUrlPattern;

	@Resource
	private StockDao stockDao;

	@Resource
	private PriceDao priceDao;

	@Override
	@Transactional
	public void updateNames(List<Stock> stocks) {
		for (Stock stock : stocks) {
			String url = msg(nameUrlPattern, stock.getTicker());
			List<String> lines = loadUrl(url);
			String name = null;
			if (!lines.isEmpty()) {
				name = lines.get(0);
				name = StringUtils.replace(name, "\"", "");
				stock.setName(name);
				stockDao.update(stock);
			}
		}
	}

	@Override
	@Transactional
	public void updatePrices(List<Stock> stocks) {

		// to_date = yesterday

		Date toDate = DateUtils.truncate(new Date(), Calendar.DATE);
		toDate = DateUtils.addDays(toDate, -1);

		for (Stock stock : stocks) {

			// from_date = last_date + 1

			Date fromDate = priceStartDate;
			List<Price> prices = priceDao.find(stock);
			if (!prices.isEmpty()) {
				Price price = prices.get(prices.size() - 1);
				Date lastDate = price.getDate();
				fromDate = DateUtils.addDays(lastDate, 1);
			}

			prices = new ArrayList<Price>();

			Date fromDate2 = fromDate;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fromDate2);

			while (fromDate2.compareTo(toDate) <= 0) {
				calendar.add(Calendar.DATE, priceIncrement);
				Date toDate2 = calendar.getTime();
				if (toDate2.compareTo(toDate) > 0) {
					toDate2 = toDate;
				}
				List<Price> prices2 = loadPrices(stock, fromDate2, toDate2);
				prices.addAll(prices2);
				logger.debug(
						msg("update stock=[{0}] from=[{1,date,yyyy-MM-dd}] to=[{2,date,yyyy-MM-dd}] : prices=[{3,number,0}]",
								stock.getTicker(), fromDate2, toDate2, prices2.size()));

				calendar.add(Calendar.DATE, 1);
				fromDate2 = calendar.getTime();

			}

			priceDao.insert(stock, prices);

			logger.info(
					msg("update stock=[{0}] from=[{1,date,yyyy-MM-dd}] to=[{2,date,yyyy-MM-dd}] : prices=[{3,number,0}]",
							stock.getTicker(), fromDate, toDate, prices.size()));
		}
	}

	private List<Price> loadPrices(Stock stock, Date fromDate, Date toDate) {

		Assert.notNull(stock);
		Assert.notNull(stock.getTicker());
		Assert.notNull(fromDate);
		Assert.notNull(toDate);
		Assert.isTrue(fromDate.compareTo(toDate) <= 0);

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(toDate);
		String url = msg(priceUrlPattern, stock.getTicker(), fromCalendar.get(Calendar.MONTH),
				fromCalendar.get(Calendar.DATE), fromCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
				toCalendar.get(Calendar.DATE), toCalendar.get(Calendar.YEAR));

		List<String> lines = loadUrl(url);
		if (!lines.isEmpty()) {
			lines = lines.subList(1, lines.size());
		}

		List<Price> prices = new ArrayList<Price>();
		for (String line : lines) {
			Price price = parsePrice(line);
			prices.add(price);
		}
		Collections.sort(prices);

		logger.debug(msg("load stock=[{0}] from=[{1,date,yyyy-MM-dd}] to=[{2,date,yyyy-MM-dd}] : prices=[{3,number,0}]",
				stock.getTicker(), fromDate, toDate, prices.size()));

		return prices;

	}

	private Price parsePrice(String line) {

		Price price = new Price();

		String[] tokens = StringUtils.delimitedListToStringArray(line, ",");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

		return price;

	}

	private List<String> loadUrl(String url) {

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
		} catch (IOException ex) {
			logger.warn(msg("failed to load url=[{0}]", url));
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				// ignore
			}
		}

		logger.debug(msg("load url=[{0}] : lines=[{1,number,0}]", url, lines.size()));

		return lines;

	}

}
