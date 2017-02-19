package com.grouplia.pmslia.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseServiceImpl {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String msg2(String pattern, Object... arguments) {
		return String.format(pattern, arguments);
	}

}
