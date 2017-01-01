package com.grouplia.pmslia;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String DATE_PATTERN = "yyyy-MM-dd";

	protected String msg(String pattern, Object... arguments) {
		return MessageFormat.format(pattern, arguments);
	}

	protected Date parseDate(String s) {
		return parseDate(s, DATE_PATTERN);
	}

	protected Date parseDate(String s, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(s);
		} catch (ParseException ex) {
			throw new IllegalArgumentException(msg("failed to parse date [{0}] with pattern [{1}]", s, pattern), ex);
		}
	}

}
