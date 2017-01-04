package com.grouplia.pmslia;

import java.io.File;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String DATE_PATTERN = "yyyy-MM-dd";

	protected String msg(String pattern, Object... arguments) {
		return MessageFormat.format(pattern, arguments);
	}
	
	protected String msg2(String pattern, Object... arguments) {
		return String.format(pattern, arguments);
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

	protected File chooseFile() {
		return chooseFile(".");
	}

	protected File chooseFile(String currentFolder) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(currentFolder));
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}

}
