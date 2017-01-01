package com.grouplia.pmslia.service.impl;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseServiceImpl {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String msg(String pattern, Object... arguments) {
		return MessageFormat.format(pattern, arguments);
	}

	public BaseServiceImpl() {
		logger.debug(msg("instantiate {0}", getClass()));
	}

}
