package com.grouplia.pmslia.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.grouplia.pmslia.domain.Stock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/service-test-config.xml" })
public class StockServiceTest extends BaseServiceTest {

	@Resource
	private StockService stockService;

	@Test
	@Transactional
	public void testService() throws Exception {
		Stock parent = new Stock("^IXIC");
		Stock child = new Stock("YHOO");
		List<Stock> children = new ArrayList<Stock>();
		children.add(child);
		Assert.assertTrue(parent != null);
		stockService.create(parent, children);
	}

}
