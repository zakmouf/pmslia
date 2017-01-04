package com.grouplia.pmslia.domain;

import java.io.Serializable;

public abstract class BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	public BaseObject() {

	}

	protected String msg(String pattern, Object... arguments) {
		return String.format(pattern, arguments);
	}

}
