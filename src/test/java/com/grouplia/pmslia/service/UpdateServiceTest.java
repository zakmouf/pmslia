package com.grouplia.pmslia.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/service-test-config.xml" })
public class UpdateServiceTest extends BaseServiceTest {

	@Resource
	private StockDao stockDao;

	@Resource
	private UpdateService updateService;

	@Test
	@Transactional
	public void testName() {

		String ticker = "YHOO";
		Stock stock = stockDao.findStockByTicker(ticker);
		if (stock == null) {
			stock = new Stock(ticker);
			stockDao.insertStock(stock);
		}

		List<Stock> stocks = new ArrayList<Stock>();
		stocks.add(stock);

		updateService.updateNames(stocks);

	}

	@Test
	@Transactional
	public void testPriceWoutLast() {

		String ticker = "YHOO";
		Stock stock = stockDao.findStockByTicker(ticker);
		if (stock == null) {
			stock = new Stock(ticker);
			stockDao.insertStock(stock);
		}
		stockDao.deletePrices(stock);

		List<Stock> stocks = new ArrayList<Stock>();
		stocks.add(stock);

		updateService.updatePrices(stocks);

	}

	@Test
	@Transactional
	public void testPriceWithLast() {

		String ticker = "YHOO";
		Stock stock = stockDao.findStockByTicker(ticker);
		if (stock == null) {
			stock = new Stock(ticker);
			stockDao.insertStock(stock);
		}
		stockDao.deletePrices(stock);

		Price price = new Price(parseDate("2013-01-01"), 123.45D);
		stockDao.insertPrice(stock, price);

		List<Stock> stocks = new ArrayList<Stock>();
		stocks.add(stock);

		updateService.updatePrices(stocks);

	}

}
