package com.grouplia.pmslia.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.domain.Holding;
import com.grouplia.pmslia.domain.Portfolio;
import com.grouplia.pmslia.domain.Price;
import com.grouplia.pmslia.domain.Stock;
import com.grouplia.pmslia.service.PreviewService;
import com.grouplia.pmslia.service.domain.PreviewConfig;
import com.grouplia.pmslia.service.domain.PreviewException;
import com.grouplia.pmslia.service.domain.PreviewResult;
import com.grouplia.pmslia.service.domain.Randomizer;
import com.grouplia.pmslia.service.util.MeasureHolder;
import com.grouplia.pmslia.service.util.PortfolioValuator;
import com.grouplia.pmslia.service.util.PriceHolder;
import com.grouplia.pmslia.service.util.PriceUtils;

@Service("previewService")
public class PreviewServiceImpl extends BaseServiceImpl implements PreviewService {

	@Resource
	private StockDao stockDao;

	private void validateStocks(PreviewConfig config, List<Stock> indiceStocks, List<Stock> previewStocks)
			throws PreviewException {
		if (previewStocks.size() < config.getMinStockCount()) {
			throw new PreviewException(
					msg("only [{0,number,0}] stocks of indice [{1}] match dates, needed [{2,number,0}]",
							previewStocks.size(), config.getIndice().getName(), indiceStocks.size()));
		}
	}

	@Override
	public PreviewResult process(PreviewConfig config) throws PreviewException {

		Stock indice = config.getIndice();
		Date fromDate = config.getFromDate();
		Date toDate = config.getToDate();

		Integer minDateCount = config.getMinDateCount();

		List<Price> indicePrices = stockDao.findPricesBetween(indice, fromDate, toDate);
		logger.debug(msg2("indice=[%1$s] from=[%2$tF] to=[%3$tF] : prices=[%4$d]", indice.getTicker(), fromDate, toDate,
				indicePrices.size()));

		if (indicePrices.size() < minDateCount) {
			throw new PreviewException(
					msg2("indice=[%1$s] from=[%2$tF] to=[%3$tF] : prices=[%4$d], minDateCount=[%5$d]",
							indice.getTicker(), fromDate, toDate, indicePrices.size(), minDateCount));
		}

		Date effectiveFromDate = PriceUtils.getFirstDate(indicePrices);
		Date effectiveToDate = PriceUtils.getLastDate(indicePrices);
		logger.debug(msg2("fromDate=[%1$tF] : effectiveFromDate=[%2$tF]", fromDate, effectiveFromDate));
		logger.debug(msg2("toDate=[%1$tF] : effectiveToDate=[%2$tF]", toDate, effectiveToDate));

		List<Stock> indiceStocks = stockDao.findChildren(indice);
		logger.debug(msg("indice [{0}] has [{1,number,0}] stocks", indice.getName(), indiceStocks.size()));

		List<Stock> previewStocks = new ArrayList<Stock>();
		PriceHolder priceHolder = new PriceHolder();
		priceHolder.addPrices(indice, indicePrices);

		for (Stock stock : indiceStocks) {
			logger.debug(msg2("load stock [%1$s] between inclusive [%2$tF] and [%3$tF]", stock.getTicker(),
					effectiveFromDate, effectiveToDate));
			List<Price> stockPrices = stockDao.findPricesBetweenInclusive(stock, effectiveFromDate, effectiveToDate);
			logger.debug(
					msg("stock [%1$s] has [%2$d] datas between inclusive [%3$tF] and [%4$tF]",
							stock.getName(), stockPrices.size(), effectiveFromDate, effectiveToDate));
			if (!stockPrices.isEmpty()) {
				previewStocks.add(stock);
				priceHolder.addPrices(stock, stockPrices);
			}
		}

		logger.debug(msg("use [{0,number,0}] of [{1,number,0}] indice [{2}] stocks", previewStocks.size(),
				indiceStocks.size(), indice.getName()));
		validateStocks(config, indiceStocks, previewStocks);

		PreviewResult result = new PreviewResult();
		Randomizer randomizer = new Randomizer(config.getBasketSize(), previewStocks.size());
		MeasureHolder measureHolder = new MeasureHolder(config.getRiskFreeRate());
		measureHolder.setIndicePrices(indicePrices);

		logger.debug(msg("iterate [{0,number,0}] times", config.getPortfolioCount()));
		for (int i = 0; i < config.getPortfolioCount(); i++) {

			int[] keys = randomizer.nextBasket();

			Portfolio portfolio = new Portfolio();
			List<Holding> holdings = new ArrayList<Holding>();
			portfolio.setIndice(indice);
			for (int j = 0; j < keys.length; j++) {
				Stock stock = previewStocks.get(keys[j]);
				Price firstPrice = priceHolder.getPrice(stock, effectiveFromDate);
				Double firstValue = firstPrice.getValue();
				Double quantity = 100D / keys.length / firstValue;
				Holding holding = new Holding(quantity, stock);
				holdings.add(holding);
			}

			List<Price> portfolioPrices = PortfolioValuator.valuate(portfolio, holdings, effectiveFromDate,
					effectiveToDate, priceHolder);
			measureHolder.setPortfolioPrices(portfolioPrices);
			result.addCombinedMeasure(measureHolder.getCombinedMeasure());

		}

		return result;
	}

}
