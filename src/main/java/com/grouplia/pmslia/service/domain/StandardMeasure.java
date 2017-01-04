package com.grouplia.pmslia.service.domain;

import com.grouplia.pmslia.domain.BaseObject;

public class StandardMeasure extends BaseObject {

	private static final long serialVersionUID = 1L;

	private Double averageReturn;
	private Double standardDeviation;
	private Double sharpRatio;

	public Double getAverageReturn() {
		return averageReturn;
	}

	public void setAverageReturn(Double averageReturn) {
		this.averageReturn = averageReturn;
	}

	public Double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(Double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public Double getSharpRatio() {
		return sharpRatio;
	}

	public void setSharpRatio(Double sharpRatio) {
		this.sharpRatio = sharpRatio;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(msg("averageReturn=%1$.5f%%", averageReturn * 100.0));
		buf.append(msg(",standardDeviation=%1$.5f%%", standardDeviation * 100.0));
		buf.append(msg(",sharpRatio=%1$.5f", sharpRatio));
		return buf.toString();
	}

}
