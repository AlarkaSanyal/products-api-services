package org.solution.as.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.solution.as.AbstractControllerTest;
import org.solution.as.model.Product;
import org.solution.as.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ProductControllerMocksTest extends AbstractControllerTest {
	
	@Mock
	private ProductService productService;
	
	@InjectMocks
	private ProductController productController;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		super.setUp(productController);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_getProduct_ok() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, "Test product name", new BigDecimal("0.75"), "USD");
		
		when(productService.findProductById(id)).thenReturn(product);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		verify(productService, times(1)).findProductById(id);
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 200, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("id ", 16696652, jsonObject.get("id"));
		assertEquals("Price value ", new BigDecimal("0.75"), ((Map<String, Object>)jsonObject.get("current_price")).get("value"));
		assertEquals("Price currencycode ", "USD", ((Map<String, Object>)jsonObject.get("current_price")).get("currencycode"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_getProduct_notFound() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("123456789");
		
		when(productService.findProductById(id)).thenReturn(null);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		verify(productService, times(1)).findProductById(id);
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 404, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("Message ", "Item not found", jsonObject.get("message"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_updateProductPrice_ok() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, "Test product name", new BigDecimal("0.75"), "USD");
		
		when(productService.updateProductById(any(Product.class))).thenReturn(true);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		verify(productService, times(1)).updateProductById(any(Product.class));
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 200, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("Message ", "Price has been updated", jsonObject.get("message"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_updateProductPrice_withoutName_ok() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, null, new BigDecimal("0.75"), "USD");
		
		when(productService.updateProductById(any(Product.class))).thenReturn(true);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		verify(productService, times(1)).updateProductById(any(Product.class));
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 200, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("Message ", "Price has been updated", jsonObject.get("message"));
	}
	
	@Test
	public void test_updateProductPrice_idNotMatching_NotModified() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(new BigInteger("123456789"), "Test product name", new BigDecimal("0.75"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 304, status);
	}
	
	@Test
	public void test_updateProductPrice_idNotFound_NotModified() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("123456789");
		Product product = getProductObject(id, null, new BigDecimal("0.75"), "USD");
		
		when(productService.updateProductById(any(Product.class))).thenReturn(false);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		verify(productService, times(1)).updateProductById(any(Product.class));
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 304, status);
	}
	
	@Test
	public void test_updateProductPrice_withNullPriceValue_BadRequest() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, "Test product name", null, "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	@Test
	public void test_updateProductPrice_withInvalidPriceValue_BadRequest() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, "Test product name", new BigDecimal("100.7559"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	@Test
	public void test_updateProductPrice_withPriceCurrencyCode_caseSensitive_BadRequest() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, "Test product name", new BigDecimal("0.75"), "usd");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	@Test
	public void test_updateProductPrice_withInvalidPriceCurrencyCode_BadRequest() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, "Test product name", new BigDecimal("0.75"), "invalid");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	@Test
	public void test_updateProductPrice_withNullPriceCurrencyCode_BadRequest() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = getProductObject(id, "Test product name", new BigDecimal("0.75"), null);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(product)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
}
