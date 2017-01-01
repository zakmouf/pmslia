package com.grouplia.pmslia.dao;

import java.util.Date;
import java.util.List;

import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;

public interface PriceDao {

	Date findLastDate(Stock stock);

	List<Price> find(Stock stock);

	void insert(Stock stock, Price price);

	void insert(Stock stock, List<Price> prices);

	void delete(Stock stock);

	void delete(Stock stock, Price price);
	
	// finders

	List<Price> findFrom(Stock stock, Date fromDate);

	List<Price> findFromInclusive(Stock stock, Date fromDate);

	List<Price> findBetween(Stock stock, Date fromDate, Date toDate);

	List<Price> findBetweenInclusive(Stock stock, Date fromDate, Date toDate);

	List<Price> loadBetween(Stock stock, Date fromDate, Date toDate);

}
