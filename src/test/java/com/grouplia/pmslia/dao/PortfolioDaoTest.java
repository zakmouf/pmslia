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
	public void testDao() {

		Stock indice = new Stock("iticker");
		stockDao.insertStock(indice);

		//Stock stock = new Stock("sticker");
		//stockDao.insert(stock);
		//Holding holding = new Holding(123.45D, stock);

		Portfolio portfolio = new Portfolio("pname", parseDate("2012-01-01"), indice);
		//portfolio.getHoldings().add(holding);

		portfolioDao.insert(portfolio);
		Assert.assertNotNull(portfolio.getId());

		portfolio = portfolioDao.findById(portfolio.getId());
		Assert.assertNotNull(portfolio);

		List<Portfolio> portfolios = portfolioDao.findAll();
		Assert.assertTrue(portfolios.contains(portfolio));

		portfolioDao.delete(portfolio);
		Assert.assertNull(portfolioDao.findById(portfolio.getId()));

	}

}
