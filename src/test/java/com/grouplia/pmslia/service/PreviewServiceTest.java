package com.grouplia.pmslia.service;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.domain.Stock;
import com.grouplia.pmslia.service.domain.CombinedMeasure;
import com.grouplia.pmslia.service.domain.PreviewConfig;
import com.grouplia.pmslia.service.domain.PreviewResult;
import com.grouplia.pmslia.service.domain.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/service-test-config.xml" })
public class PreviewServiceTest extends BaseServiceTest {

	@Resource
	private StockDao stockDao;

	@Resource
	private PreviewService service;

	@Test
	public void testService() throws ServiceException {

		Stock indice = stockDao.findStockByTicker("^GDAXI");
		Assert.assertNotNull(indice);

		PreviewConfig config = new PreviewConfig();
		config.setIndice(indice);
		config.setFromDate(parseDate("2015-07-01"));
		config.setToDate(parseDate("2016-06-30"));
		// config.setRiskFreeRate(0.03);
		// config.setMinDateCount(10);
		// config.setMinStockCount(10);
		// config.setBasketSize(5);
		// config.setPortfolioCount(20);

		PreviewResult result = service.process(config);

		logger.debug("no;pf_mean;pf_stdev,beta");
		int i = 1;
		for (CombinedMeasure combinedMeasure : result.getCombinedMeasures()) {
			logger.debug(msg2("%1$d;%2$.5f%%;%3$.5f%%;%4$.5f;%5$.5f%%", i++,
					combinedMeasure.getPortfolioMeasure().getAverageReturn() * 100.0,
					combinedMeasure.getPortfolioMeasure().getStandardDeviation() * 100.0,
					combinedMeasure.getRegressionMeasure().getBeta(),
					combinedMeasure.getRegressionMeasure().getDecisionRatio() * 100.0));
		}

	}

}
