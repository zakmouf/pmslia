package com.grouplia.pmslia.service;

import java.util.List;

import com.grouplia.pmslia.domain.Stock;

public interface StockService {

	void create(Stock parent, List<Stock> children);

}
