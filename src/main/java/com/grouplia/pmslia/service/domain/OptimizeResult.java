package com.grouplia.pmslia.service.domain;

import java.util.ArrayList;
import java.util.List;

import com.grouplia.pmslia.domain.BaseObject;

public class OptimizeResult extends BaseObject {

	private static final long serialVersionUID = 1L;

	private List<CombinedMeasure> combinedMeasures = new ArrayList<CombinedMeasure>();

	public List<CombinedMeasure> getCombinedMeasures() {
		return combinedMeasures;
	}

	public void addCombinedMeasure(CombinedMeasure combinedMeasure) {
		combinedMeasures.add(combinedMeasure);
	}

}
