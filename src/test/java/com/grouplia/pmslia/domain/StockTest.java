package com.grouplia.pmslia.domain;

import org.junit.Test;

import com.grouplia.pmslia.BaseTest;

public class StockTest extends BaseTest {

	@Test
	public void testDomain() {
		Stock stock = new Stock("sname");
		logger.debug(msg("stock:{0}", stock));
	}

}
