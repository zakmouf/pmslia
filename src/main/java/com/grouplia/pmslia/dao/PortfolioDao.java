package com.grouplia.pmslia.dao;

import java.util.List;

import com.grouplia.pmslia.domain.Holding;
import com.grouplia.pmslia.domain.Portfolio;

public interface PortfolioDao {

	List<Portfolio> findPortfolios();

	Portfolio findPortfolioById(Long id);

	void insertPortfolio(Portfolio portfolio);

	void updatePortfolio(Portfolio portfolio);

	void deletePortfolio(Portfolio portfolio);

	List<Holding> findHoldings(Portfolio portfolio);

	void insertHolding(Portfolio portfolio, Holding holding);

	void insertHoldings(Portfolio portfolio, List<Holding> holdings);

	void deleteHolding(Portfolio portfolio, Holding holding);

	void deleteHoldings(Portfolio portfolio);

}
