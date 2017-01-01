package com.grouplia.pmslia.domain;

public abstract class BaseEntity extends BaseObject {

	private static final long serialVersionUID = 1L;

	protected Long id;

	public BaseEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
