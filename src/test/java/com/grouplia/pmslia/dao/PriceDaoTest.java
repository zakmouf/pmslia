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

import com.zakmouf.pms.domain.Price;
import com.zakmouf.pms.domain.Stock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/dao-test-config.xml" })
public class PriceDaoTest extends BaseDaoTest {

    @Resource
    private StockDao stockDao;

    @Resource
    private PriceDao priceDao;

    @Test
    @Transactional
    public void testDao() {

	Stock stock = new Stock("sname");
	stockDao.insert(stock);

	Date date0 = parseDate("2012-12-14");
	Date date1 = parseDate("2012-12-15");
	Date date2 = parseDate("2012-12-16");
	Date date3 = parseDate("2012-12-17");
	Date date4 = parseDate("2012-12-18");
	Date date5 = parseDate("2012-12-19");
	Date date6 = parseDate("2012-12-20");

	Price price1 = new Price(date1, 1.1d);
	Price price3 = new Price(date3, 3.3d);
	Price price5 = new Price(date5, 5.5d);

	List<Price> prices;

	prices = priceDao.find(stock);
	Assert.assertTrue(prices.isEmpty());

	priceDao.insert(stock, price1);
	priceDao.insert(stock, price3);
	priceDao.insert(stock, price5);
	prices = priceDao.find(stock);
	Assert.assertFalse(prices.isEmpty());
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));

	prices = priceDao.findFrom(stock, date0);
	Assert.assertEquals(prices.size(), 3);
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFrom(stock, date1);
	Assert.assertEquals(prices.size(), 3);
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFrom(stock, date2);
	Assert.assertEquals(prices.size(), 2);
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFrom(stock, date3);
	Assert.assertEquals(prices.size(), 2);
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFrom(stock, date4);
	Assert.assertEquals(prices.size(), 1);
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFrom(stock, date5);
	Assert.assertEquals(prices.size(), 1);
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFrom(stock, date6);
	Assert.assertTrue(prices.isEmpty());

	prices = priceDao.findFromInclusive(stock, date0);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findFromInclusive(stock, date1);
	Assert.assertEquals(prices.size(), 3);
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFromInclusive(stock, date2);
	Assert.assertEquals(prices.size(), 3);
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFromInclusive(stock, date3);
	Assert.assertEquals(prices.size(), 2);
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFromInclusive(stock, date4);
	Assert.assertEquals(prices.size(), 2);
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFromInclusive(stock, date5);
	Assert.assertEquals(prices.size(), 1);
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findFromInclusive(stock, date6);
	Assert.assertEquals(prices.size(), 1);
	Assert.assertTrue(prices.contains(price5));

	prices = priceDao.findBetween(stock, date0, date0);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetween(stock, date0, date6);
	Assert.assertEquals(prices.size(), 3);
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetween(stock, date5, date5);
	Assert.assertEquals(prices.size(), 1);
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetween(stock, date5, date6);
	Assert.assertEquals(prices.size(), 1);
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetween(stock, date6, date6);
	Assert.assertTrue(prices.isEmpty());

	prices = priceDao.findBetweenInclusive(stock, date0, date0);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date0, date1);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date0, date2);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date0, date3);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date0, date4);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date0, date5);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date0, date6);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date1, date5);
	Assert.assertEquals(prices.size(), 3);
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetweenInclusive(stock, date2, date4);
	Assert.assertEquals(prices.size(), 3);
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetweenInclusive(stock, date3, date5);
	Assert.assertEquals(prices.size(), 2);
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetweenInclusive(stock, date4, date5);
	Assert.assertEquals(prices.size(), 2);
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetweenInclusive(stock, date5, date5);
	Assert.assertEquals(prices.size(), 1);
	Assert.assertTrue(prices.contains(price5));
	prices = priceDao.findBetweenInclusive(stock, date0, date6);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date1, date6);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date2, date6);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date3, date6);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date4, date6);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date5, date6);
	Assert.assertTrue(prices.isEmpty());
	prices = priceDao.findBetweenInclusive(stock, date6, date6);
	Assert.assertTrue(prices.isEmpty());

	priceDao.delete(stock, price1);
	prices = priceDao.find(stock);
	Assert.assertFalse(prices.isEmpty());
	Assert.assertFalse(prices.contains(price1));

	priceDao.delete(stock, price3);
	prices = priceDao.find(stock);
	Assert.assertFalse(prices.isEmpty());
	Assert.assertFalse(prices.contains(price3));

	priceDao.delete(stock, price5);
	prices = priceDao.find(stock);
	Assert.assertTrue(prices.isEmpty());

	priceDao.insert(stock, price1);
	priceDao.insert(stock, price3);
	priceDao.insert(stock, price5);
	prices = priceDao.find(stock);
	Assert.assertFalse(prices.isEmpty());
	Assert.assertTrue(prices.contains(price1));
	Assert.assertTrue(prices.contains(price3));
	Assert.assertTrue(prices.contains(price5));
	priceDao.delete(stock);
	prices = priceDao.find(stock);
	Assert.assertTrue(prices.isEmpty());

    }

    @Test
    @Transactional
    public void testLoad() {

	String name = "YHOO";
	Stock stock = stockDao.findByName(name);
	if (stock == null) {
	    stock = new Stock();
	    stock.setName(name);
	    stockDao.insert(stock);
	}

	Date fromDate = parseDate("2012-01-01");
	Date toDate = parseDate("2012-12-31");

	List<Price> prices;

	prices = priceDao.loadBetween(stock, fromDate, fromDate);
	Assert.assertEquals(prices.size(), 0);

	prices = priceDao.loadBetween(stock, fromDate, toDate);
	Assert.assertEquals(prices.size(), 250);

	prices = priceDao.loadBetween(stock, toDate, toDate);
	Assert.assertEquals(prices.size(), 1);

    }

}
