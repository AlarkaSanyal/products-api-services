package org.solution.as.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import org.junit.Test;
import org.solution.as.AbstractTest;
import org.solution.as.model.Price;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alarka
 * 
 * Data added to TABLE price at start of application
 * 
 * id       | currencycode | value
 * ---------+--------------+---------
 * 16696652 |          USD |    0.75
 * 15643793 |          USD |   99.99
 * 15117729 |          USD |   25.25
 * 16483589 |          USD | 5000.50
 * 16752456 |          USD |      99
 * 13860428 |          USD |   16.65
 */

public class PriceRepositoryTest extends AbstractTest {

	@Autowired
	private PriceRepository priceRepository;
	
	@Test
	public void test_findPrices() {
		Collection<Price> prices = priceRepository.findPrices();
		assertEquals("Expected count: ", 6, prices.size());
	}
	
	@Test
	public void test_findPriceById() {
		Price price = priceRepository.findPriceById(new BigInteger("16696652"));
		assertEquals("Expected currency code: ", "USD", price.getCurrencycode());
		assertEquals("Expected value: ", new BigDecimal("0.75"), price.getValue());
	}
	
	@Test
	public void test_insertPriceById() {
		priceRepository.insertPriceById(new BigInteger("123456789"), new BigDecimal("50.50"), "USD");
		Price price = priceRepository.findPriceById(new BigInteger("123456789"));
		assertEquals("Expected currency code: ", "USD", price.getCurrencycode());
		assertEquals("Expected value: ", new BigDecimal("50.50"), price.getValue());
		
		// Rollback
		priceRepository.deletePriceById(new BigInteger("123456789"));
	}
	
	@Test
	public void test_deletePriceById() {
		priceRepository.deletePriceById(new BigInteger("16696652"));
		Price price = priceRepository.findPriceById(new BigInteger("16696652"));
		assertNull("Price should not be present ", price);
		
		// Rollback
		priceRepository.insertPriceById(new BigInteger("16696652"), new BigDecimal("0.75"), "USD");
	}
	
}
