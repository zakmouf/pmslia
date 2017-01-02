package com.grouplia.pmslia.service.domain;

import com.grouplia.pmslia.domain.BaseObject;

public class CombinedMeasure extends BaseObject {

	private static final long serialVersionUID = 1L;

	private StandardMeasure portfolioMeasure;
	private StandardMeasure indiceMeasure;
	private RegressionMeasure regressionMeasure;

	public StandardMeasure getIndiceMeasure() {
		return indiceMeasure;
	}

	public void setIndiceMeasure(StandardMeasure indiceMeasure) {
		this.indiceMeasure = indiceMeasure;
	}

	public StandardMeasure getPortfolioMeasure() {
		return portfolioMeasure;
	}

	public void setPortfolioMeasure(StandardMeasure portfolioMeasure) {
		this.portfolioMeasure = portfolioMeasure;
	}

	public RegressionMeasure getRegressionMeasure() {
		return regressionMeasure;
	}

	public void setRegressionMeasure(RegressionMeasure regressionMeasure) {
		this.regressionMeasure = regressionMeasure;
	}

	@Override
	public String toString() {
		return msg(
				"" //
						+ "\n  portfolio=[{0}]"//
						+ "\n  indice=[{1}]" //
						+ "\n  regression=[{2}]\n"//
				, portfolioMeasure, indiceMeasure, regressionMeasure);
	}

}
