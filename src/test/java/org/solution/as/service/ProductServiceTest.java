package org.solution.as.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.solution.as.AbstractTest;
import org.solution.as.model.Price;
import org.solution.as.model.Product;
import org.solution.as.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;

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
 *
 */

public class ProductServiceTest extends AbstractTest {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Before
	public void setUp() {
		service.emptyCache();
	}
	
	@Test
	public void test_findProductById() throws JsonProcessingException, IOException {
		Product product = service.findProductById(new BigInteger("16696652"));
		assertEquals("Expected currency code: ", "USD", product.getCurrent_price().getCurrencycode());
		assertEquals("Expected value: ", new BigDecimal("0.75"), product.getCurrent_price().getValue());
	}
	
	@Test
	public void test_findProductById_null() throws JsonProcessingException, IOException {
		Product product = service.findProductById(new BigInteger("123456789"));
		assertNull("Product should be null ", product);
	}
	
	@Test
	public void test_updateProductById_true() throws JsonProcessingException, IOException {
		Price newPrice = new Price(new BigInteger("16696652"), new BigDecimal("25.50"), "USD");
		Product newProduct = new Product(new BigInteger("16696652"), "Update unit test", newPrice);
		boolean updated = service.updateProductById(newProduct);
		assertTrue("Price should be updated ", updated);
		
		// Assert updated price from repository
		Price updatedPrice = priceRepository.findPriceById(new BigInteger("16696652"));
		assertEquals("Expected currency code: ", "USD", updatedPrice.getCurrencycode());
		assertEquals("Expected value: ", new BigDecimal("25.50"), updatedPrice.getValue());
		
		// Rollback original values for id: 16696652
		priceRepository.insertPriceById(new BigInteger("16696652"), new BigDecimal("0.75"), "USD");
	}
	
	@Test
	public void test_updateProductById_false() throws JsonProcessingException, IOException {
		Price newPrice = new Price(new BigInteger("123456789"), new BigDecimal("25.50"), "USD");
		Product newProduct = new Product(new BigInteger("123456789"), "Update unit test", newPrice);
		boolean updated = service.updateProductById(newProduct);
		assertFalse("Price should NOT be updated ", updated);
	}
}
