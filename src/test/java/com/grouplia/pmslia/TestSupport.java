package com.grouplia.pmslia;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSupport {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void doTest() {
		logger.info("hello");
	}

}
