package com.grouplia.pmslia.domain.util;

import java.util.Date;

import com.grouplia.pmslia.domain.CashDeposit;
import com.grouplia.pmslia.domain.Currency;
import com.grouplia.pmslia.domain.Portfolio;

public abstract class TransactionUtils {

	public static void addCashDeposit(Portfolio portfolio, Date date, Currency currency, Double amount) {
		CashDeposit deposit = new CashDeposit(date, currency, amount);
		portfolio.getTransactions().add(deposit);
	}

}
