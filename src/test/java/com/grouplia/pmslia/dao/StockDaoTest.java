package com.grouplia.pmslia.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/dao-test-config.xml" })
public class StockDaoTest extends BaseDaoTest {

	@Resource
	private StockDao stockDao;

	@Test
	@Transactional
	public void testStock() {

		Stock stock = new Stock("sticker");
		stock.setName("sname");

		Assert.assertNull(stockDao.findStockByTicker(stock.getTicker()));

		stockDao.insertStock(stock);
		Assert.assertNotNull(stock.getId());

		stock = stockDao.findStockById(stock.getId());
		Assert.assertNotNull(stock);

		stock = stockDao.findStockByTicker(stock.getTicker());
		Assert.assertNotNull(stock);

		List<Stock> stocks = stockDao.findStocks();
		Assert.assertTrue(stocks.contains(stock));

		stock.setTicker("#" + stock.getTicker() + "#");
		stock.setName("#" + stock.getName() + "#");
		stockDao.updateStock(stock);

		stock = stockDao.findStockByTicker(stock.getTicker());
		Assert.assertNotNull(stock);

		stockDao.deleteStock(stock);
		Assert.assertNull(stockDao.findStockById(stock.getId()));
		Assert.assertNull(stockDao.findStockByTicker(stock.getTicker()));

	}

	@Test
	@Transactional
	public void testRelation() {

		// relation prepare

		Stock parent = new Stock("pticker");
		Stock child = new Stock("cticker");
		stockDao.insertStock(parent);
		stockDao.insertStock(child);

		List<Stock> parents;
		List<Stock> children;

		// relation before insert

		children = stockDao.findChildren(parent);
		Assert.assertTrue(children.isEmpty());

		parents = stockDao.findParents(child);
		Assert.assertTrue(parents.isEmpty());

		// relation after insert

		stockDao.insertRelation(parent, child);

		children = stockDao.findChildren(parent);
		Assert.assertTrue(children.contains(child));

		parents = stockDao.findParents(child);
		Assert.assertTrue(parents.contains(parent));

		// relation after delete

		stockDao.deleteRelation(parent, child);

		children = stockDao.findChildren(parent);
		Assert.assertTrue(children.isEmpty());

		parents = stockDao.findParents(child);
		Assert.assertTrue(parents.isEmpty());

		// relation clean

		stockDao.deleteStock(parent);
		stockDao.deleteStock(child);

		// relation cascade parent

		parent = new Stock("pticker");
		child = new Stock("cticker");
		stockDao.insertStock(parent);
		stockDao.insertStock(child);
		stockDao.insertRelation(parent, child);
		stockDao.deleteStock(parent);
		stockDao.deleteStock(child);

		// relation cascade child

		parent = new Stock("pticker");
		child = new Stock("cticker");
		stockDao.insertStock(parent);
		stockDao.insertStock(child);
		stockDao.insertRelation(parent, child);
		stockDao.deleteStock(child);
		stockDao.deleteStock(parent);

	}

	private Date date0 = parseDate("2012-12-14");
	private Date date1 = parseDate("2012-12-15");
	private Date date2 = parseDate("2012-12-16");
	private Date date3 = parseDate("2012-12-17");
	private Date date4 = parseDate("2012-12-18");
	private Date date5 = parseDate("2012-12-19");
	private Date date6 = parseDate("2012-12-20");

	private Price price1 = new Price(date1, 1.1d);
	private Price price3 = new Price(date3, 3.3d);
	private Price price5 = new Price(date5, 5.5d);

	@Test
	@Transactional
	public void testPrice() {

		Stock stock = new Stock("sticker");
		stockDao.insertStock(stock);

		List<Price> prices;

		prices = stockDao.findPrices(stock);
		Assert.assertTrue(prices.isEmpty());
		Assert.assertNull(stockDao.findLastPrice(stock));

		stockDao.insertPrice(stock, price1);
		stockDao.insertPrice(stock, price3);
		stockDao.insertPrice(stock, price5);
		prices = stockDao.findPrices(stock);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		Assert.assertEquals(stockDao.findLastPrice(stock), price5);

		stockDao.deletePrice(stock, price1);
		prices = stockDao.findPrices(stock);
		Assert.assertEquals(prices.size(), 2);
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		Assert.assertEquals(stockDao.findLastPrice(stock), price5);

		stockDao.deletePrice(stock, price3);
		prices = stockDao.findPrices(stock);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));
		Assert.assertEquals(stockDao.findLastPrice(stock), price5);

		stockDao.deletePrice(stock, price5);
		prices = stockDao.findPrices(stock);
		Assert.assertTrue(prices.isEmpty());
		Assert.assertNull(stockDao.findLastPrice(stock));

		stockDao.insertPrice(stock, price1);
		stockDao.insertPrice(stock, price3);
		stockDao.insertPrice(stock, price5);
		prices = stockDao.findPrices(stock);
		Assert.assertEquals(prices.size(), 3);
		stockDao.deletePrices(stock);
		prices = stockDao.findPrices(stock);
		Assert.assertTrue(prices.isEmpty());

		// price cascade

		stockDao.insertPrice(stock, price1);
		stockDao.insertPrice(stock, price3);
		stockDao.insertPrice(stock, price5);
		prices = stockDao.findPrices(stock);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		stockDao.deleteStock(stock);

	}

	@Test
	@Transactional
	public void testPrice2() {

		Stock stock = new Stock("sticker");
		stockDao.insertStock(stock);

		List<Price> prices;

		stockDao.insertPrice(stock, price1);
		stockDao.insertPrice(stock, price3);
		stockDao.insertPrice(stock, price5);

		prices = stockDao.findPricesFrom(stock, date0);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFrom(stock, date1);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFrom(stock, date2);
		Assert.assertEquals(prices.size(), 2);
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFrom(stock, date3);
		Assert.assertEquals(prices.size(), 2);
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFrom(stock, date4);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFrom(stock, date5);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFrom(stock, date6);
		Assert.assertTrue(prices.isEmpty());

		prices = stockDao.findPricesFromInclusive(stock, date0);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesFromInclusive(stock, date1);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFromInclusive(stock, date2);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFromInclusive(stock, date3);
		Assert.assertEquals(prices.size(), 2);
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFromInclusive(stock, date4);
		Assert.assertEquals(prices.size(), 2);
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFromInclusive(stock, date5);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesFromInclusive(stock, date6);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));

		prices = stockDao.findPricesBetween(stock, date0, date0);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetween(stock, date0, date6);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetween(stock, date5, date5);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetween(stock, date5, date6);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetween(stock, date6, date6);
		Assert.assertTrue(prices.isEmpty());

		prices = stockDao.findPricesBetweenInclusive(stock, date0, date0);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date0, date1);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date0, date2);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date0, date3);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date0, date4);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date0, date5);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date0, date6);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date1, date5);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetweenInclusive(stock, date2, date4);
		Assert.assertEquals(prices.size(), 3);
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetweenInclusive(stock, date3, date5);
		Assert.assertEquals(prices.size(), 2);
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetweenInclusive(stock, date4, date5);
		Assert.assertEquals(prices.size(), 2);
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetweenInclusive(stock, date5, date5);
		Assert.assertEquals(prices.size(), 1);
		Assert.assertTrue(prices.contains(price5));
		prices = stockDao.findPricesBetweenInclusive(stock, date0, date6);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date1, date6);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date2, date6);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date3, date6);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date4, date6);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date5, date6);
		Assert.assertTrue(prices.isEmpty());
		prices = stockDao.findPricesBetweenInclusive(stock, date6, date6);
		Assert.assertTrue(prices.isEmpty());

		stockDao.deletePrice(stock, price1);
		prices = stockDao.findPrices(stock);
		Assert.assertFalse(prices.isEmpty());
		Assert.assertFalse(prices.contains(price1));

		stockDao.deletePrice(stock, price3);
		prices = stockDao.findPrices(stock);
		Assert.assertFalse(prices.isEmpty());
		Assert.assertFalse(prices.contains(price3));

		stockDao.deletePrice(stock, price5);
		prices = stockDao.findPrices(stock);
		Assert.assertTrue(prices.isEmpty());

		stockDao.insertPrice(stock, price1);
		stockDao.insertPrice(stock, price3);
		stockDao.insertPrice(stock, price5);
		prices = stockDao.findPrices(stock);
		Assert.assertFalse(prices.isEmpty());
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		stockDao.deletePrices(stock);
		prices = stockDao.findPrices(stock);
		Assert.assertTrue(prices.isEmpty());

		// test cascade

		stockDao.insertPrice(stock, price1);
		stockDao.insertPrice(stock, price3);
		stockDao.insertPrice(stock, price5);
		prices = stockDao.findPrices(stock);
		Assert.assertFalse(prices.isEmpty());
		Assert.assertTrue(prices.contains(price1));
		Assert.assertTrue(prices.contains(price3));
		Assert.assertTrue(prices.contains(price5));
		stockDao.deletePrices(stock);
		prices = stockDao.findPrices(stock);
		Assert.assertTrue(prices.isEmpty());

	}

}
