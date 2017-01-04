package com.grouplia.pmslia.service.domain;

import com.grouplia.pmslia.domain.BaseObject;

public class RegressionMeasure extends BaseObject {

	private static final long serialVersionUID = 1L;

	private Double decisionRatio;
	private Double alpha;
	private Double beta;
	private Double alphaBear;
	private Double betaBear;
	private Double alphaBull;
	private Double betaBull;

	public Double getDecisionRatio() {
		return decisionRatio;
	}

	public void setDecisionRatio(Double decisionRatio) {
		this.decisionRatio = decisionRatio;
	}

	public Double getAlpha() {
		return alpha;
	}

	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	public Double getBeta() {
		return beta;
	}

	public void setBeta(Double beta) {
		this.beta = beta;
	}

	public Double getAlphaBear() {
		return alphaBear;
	}

	public void setAlphaBear(Double alphaBear) {
		this.alphaBear = alphaBear;
	}

	public Double getBetaBear() {
		return betaBear;
	}

	public void setBetaBear(Double betaBear) {
		this.betaBear = betaBear;
	}

	public Double getAlphaBull() {
		return alphaBull;
	}

	public void setAlphaBull(Double alphaBull) {
		this.alphaBull = alphaBull;
	}

	public Double getBetaBull() {
		return betaBull;
	}

	public void setBetaBull(Double betaBull) {
		this.betaBull = betaBull;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(msg("decisionRatio=%1$.5f%%", decisionRatio * 100.0));
		buf.append(msg(",alpha=%1$.5f%%", alpha * 100.0));
		buf.append(msg(",beta=%1$.5f", beta));
		buf.append(msg(",alphaBear=%1$.5f%%", alphaBear * 100.0));
		buf.append(msg(",betaBear=%1$.5f", betaBear));
		buf.append(msg(",alphaBull=%1$.5f%%", alphaBull * 100.0));
		buf.append(msg(",betaBull=%1$.5f", betaBull));
		return buf.toString();
	}

}
