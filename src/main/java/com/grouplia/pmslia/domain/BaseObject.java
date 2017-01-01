package com.grouplia.pmslia.domain;

import java.io.Serializable;
import java.text.MessageFormat;

public abstract class BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	public BaseObject() {

	}

	protected String msg(String pattern, Object... arguments) {
		return MessageFormat.format(pattern, arguments);
	}

}
