package com.grouplia.pmslia.service;

import java.util.Date;

import com.grouplia.pmslia.domain.Portfolio;

public interface PortfolioService {

	Double getAmount(Portfolio portfolio, Date date);

}
