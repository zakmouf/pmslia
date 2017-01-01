package com.grouplia.pmslia.service.load;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.grouplia.pmslia.domain.Stock;
import com.grouplia.pmslia.service.BaseServiceTest;
import com.grouplia.pmslia.service.StockService;

public class StockServiceLoad extends BaseServiceTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/service-test-config.xml");
		StockService stockService = (StockService) ctx.getBean("stockService");
		File file = new File("src/test/resources/sample/DAX.TXT");
		Assert.assertTrue(file.exists());
		List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
		Assert.assertTrue(lines.size() >= 1);
		Stock parent = null;
		List<Stock> children = new ArrayList<Stock>();
		for (String line : lines) {
			String ticker = StringUtils.trim(line);
			if (parent == null) {
				parent = new Stock(ticker);
			} else {
				Stock child = new Stock(ticker);
				children.add(child);
			}
		}
		Assert.assertTrue(parent != null);
		stockService.create(parent, children);
	}

}
