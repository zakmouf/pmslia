package com.grouplia.pmslia.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.grouplia.pmslia.domain.Portfolio;
import com.grouplia.pmslia.service.PortfolioService;

@Service("portfolioService")
public class PortfolioServiceImpl extends AbstractServiceImpl implements PortfolioService {

	@Override
	public Double getAmount(Portfolio portfolio, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

}
