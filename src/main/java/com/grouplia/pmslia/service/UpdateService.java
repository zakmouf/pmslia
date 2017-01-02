package com.grouplia.pmslia.service;

import java.util.List;

import com.grouplia.pmslia.domain.Stock;

public interface UpdateService {

	
	void updateNames(List<Stock> stocks);
	
	void updatePrices(List<Stock> stocks);

}
