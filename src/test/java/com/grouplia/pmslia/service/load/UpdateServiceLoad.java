package com.grouplia.pmslia.service.load;

import java.util.List;

import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.domain.Stock;
import com.grouplia.pmslia.service.BaseServiceTest;
import com.grouplia.pmslia.service.UpdateService;

public class UpdateServiceLoad extends BaseServiceTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/service-test-config.xml");
		StockDao stockDao = (StockDao) ctx.getBean("stockDao");
		UpdateService updateService = (UpdateService) ctx.getBean("updateService");
		Stock stock = stockDao.findByTicker("^GDAXI");
		Assert.assertTrue(stock != null);
		List<Stock> stocks = stockDao.findChildren(stock);
		stocks.add(stock);
		updateService.updateNames(stocks);
		updateService.updatePrices(stocks);
	}

}
