package com.grouplia.pmslia.dao;

import java.util.Date;
import java.util.List;

import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

public interface StockDao {

	// finders

	List<Stock> findStocks();

	Stock findStockById(Long id);

	Stock findStockByTicker(String ticker);

	// price finders

	Price findLastPrice(Stock stock);

	List<Price> findPrices(Stock stock);

	List<Price> findPricesFrom(Stock stock, Date fromDate);

	List<Price> findPricesFromInclusive(Stock stock, Date fromDate);

	List<Price> findPricesBetween(Stock stock, Date fromDate, Date toDate);

	List<Price> findPricesBetweenInclusive(Stock stock, Date fromDate, Date toDate);

	// stock

	void insertStock(Stock stock);

	void updateStock(Stock stock);

	void deleteStock(Stock stock);

	// relations

	List<Stock> findParents(Stock stock);

	List<Stock> findChildren(Stock stock);

	void insertRelation(Stock parent, Stock child);

	void deleteRelation(Stock parent, Stock child);

	//

	void insertPrice(Stock stock, Price price);

	void insertPrices(Stock stock, List<Price> prices);

	void deletePrice(Stock stock, Price price);

	void deletePrices(Stock stock);

}
