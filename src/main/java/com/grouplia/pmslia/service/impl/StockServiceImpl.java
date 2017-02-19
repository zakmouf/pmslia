package com.grouplia.pmslia.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grouplia.pmslia.dao.StockDao;
import com.grouplia.pmslia.domain.Stock;
import com.grouplia.pmslia.service.StockService;

@Service("stockService")
public class StockServiceImpl extends BaseServiceImpl implements StockService {

	@Resource
	private StockDao stockDao;

	@Override
	@Transactional
	public void create(Stock parent, List<Stock> children) {

		Stock existingParent = stockDao.findStockByTicker(parent.getTicker());
		if (existingParent != null) {
			parent.setId(existingParent.getId());
			logger.debug(msg2("update stock [%1$s]", parent));
			stockDao.updateStock(parent);
		} else {
			logger.debug(msg2("add stock [%1$s]", parent));
			stockDao.insertStock(parent);
		}

		List<Stock> existingChildren = stockDao.findChildren(parent);
		for (Stock existingChild : existingChildren) {
			logger.debug(msg2("remove child [%1$s] from parent [%2$s]", existingChild, parent));
			stockDao.deleteRelation(parent, existingChild);
		}

		for (Stock child : children) {
			Stock existingChild = stockDao.findStockByTicker(child.getTicker());
			if (existingChild != null) {
				child.setId(existingChild.getId());
				logger.debug(msg2("update stock [%1$s]", child));
				stockDao.updateStock(child);
			} else {
				logger.debug(msg2("add stock [%1$s]", child));
				stockDao.insertStock(child);
			}
			logger.debug(msg2("add child [%1$s] to parent [%2$s]", child, parent));
			stockDao.insertRelation(parent, child);
		}

	}

}
