package com.grouplia.pmslia.dao;

import java.util.List;

import com.grouplia.pmslia.domain.Stock;

public interface StockDao {

	List<Stock> findAll();

	Stock findById(Long id);

	Stock findByTicker(String ticker);

	void insert(Stock stock);

	void update(Stock stock);

	void delete(Stock stock);

	List<Stock> findParents(Stock stock);

	List<Stock> findChildren(Stock stock);

	void insertRelation(Stock parent, Stock child);

	void deleteRelation(Stock parent, Stock child);

}
