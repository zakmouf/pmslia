package com.grouplia.pmslia.domain;

import java.util.Date;

import org.junit.Test;

import com.grouplia.pmslia.BaseTest;

public class StockTest extends BaseTest {

	@Test
	public void testDomain() {
		Stock stock = new Stock("sticker");
		logger.debug(msg("stock:{0}", stock));
		logger.debug(String.format("text=%1$s date=%2$tF int=%3$d double=%4$.2f percent=%5$.2f%%", "hello", new Date(), 38545671245634L, 4568783.1257D, 4568783.1257D));
	}

}
