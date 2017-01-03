package com.grouplia.pmslia.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.grouplia.pmslia.domain.Holding;
import com.grouplia.pmslia.domain.Portfolio;
import com.grouplia.pmslia.domain.Stock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/dao-test-config.xml" })
public class PortfolioDaoTest extends BaseDaoTest {

	@Resource
	private StockDao stockDao;

	@Resource
	private PortfolioDao portfolioDao;

	@Test
	@Transactional
	public void testPortfolio() {

		Stock indice = new Stock("iticker");
		stockDao.insertStock(indice);

		Portfolio portfolio = new Portfolio("pname", parseDate("2012-01-01"), indice);

		Assert.assertNull(portfolio.getId());
		portfolioDao.insertPortfolio(portfolio);
		Assert.assertNotNull(portfolio.getId());

		portfolio = portfolioDao.findPortfolioById(portfolio.getId());
		Assert.assertNotNull(portfolio);

		List<Portfolio> portfolios = portfolioDao.findPortfolios();
		Assert.assertTrue(portfolios.contains(portfolio));

		portfolioDao.deletePortfolio(portfolio);
		Assert.assertNull(portfolioDao.findPortfolioById(portfolio.getId()));

	}

	@Test
	@Transactional
	public void testHolding() {

		Stock indice = new Stock("iticker");
		stockDao.insertStock(indice);
		Portfolio portfolio = new Portfolio("pname", parseDate("2012-01-01"), indice);
		portfolioDao.insertPortfolio(portfolio);

		Stock stock1 = new Stock("sticker1");
		stockDao.insertStock(stock1);
		Stock stock2 = new Stock("sticker2");
		stockDao.insertStock(stock2);
		Holding holding1 = new Holding(1.0D, stock1);
		Holding holding2 = new Holding(1.0D, stock2);

		List<Holding> holdings;

		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertTrue(holdings.isEmpty());

		portfolioDao.insertHolding(portfolio, holding1);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertEquals(holdings.size(), 1);
		Assert.assertTrue(holdings.contains(holding1));

		portfolioDao.insertHolding(portfolio, holding2);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertEquals(holdings.size(), 2);
		Assert.assertTrue(holdings.contains(holding1));
		Assert.assertTrue(holdings.contains(holding2));

		portfolioDao.deleteHolding(portfolio, holding1);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertEquals(holdings.size(), 1);
		Assert.assertTrue(holdings.contains(holding2));

		portfolioDao.deleteHolding(portfolio, holding2);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertTrue(holdings.isEmpty());

		portfolioDao.insertHolding(portfolio, holding1);
		portfolioDao.insertHolding(portfolio, holding2);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertEquals(holdings.size(), 2);
		portfolioDao.deleteHoldings(portfolio);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertTrue(holdings.isEmpty());

		portfolioDao.insertHolding(portfolio, holding1);
		portfolioDao.insertHolding(portfolio, holding2);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertEquals(holdings.size(), 2);
		portfolioDao.deletePortfolio(portfolio);
		holdings = portfolioDao.findHoldings(portfolio);
		Assert.assertTrue(holdings.isEmpty());

	}

}
