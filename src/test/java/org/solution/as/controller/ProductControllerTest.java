package org.solution.as.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.solution.as.AbstractControllerTest;
import org.solution.as.model.Product;
import org.solution.as.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ProductControllerTest extends AbstractControllerTest {
	
	@Autowired
	private ProductService productService;
	
	@Before
	public void setUp() {
		super.setUp();
		productService.emptyCache();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_getProduct_ok() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
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
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 404, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("Message ", "Item not found", jsonObject.get("message"));
	}
	
	@Test
	public void test_getProduct_idNotNumber() throws Exception {
		String uri = "/api/products/{id}";
		String id = "abcd";
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 400, status);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_updateProductPrice_ok() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(product.getId(), product.getName(), new BigDecimal("100.25"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 200, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("Message ", "Price has been updated", jsonObject.get("message"));
		
		//Rollback
		mvc.perform(MockMvcRequestBuilders.put(uri, id)
										.contentType(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.content(super.mapToJson(product)))
									.andReturn();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_updateProductPrice_withNotMatchingName_ok() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(product.getId(), "New product name", new BigDecimal("100.25"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 200, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("Message ", "Price has been updated", jsonObject.get("message"));
		
		//Rollback
		mvc.perform(MockMvcRequestBuilders.put(uri, id)
										.contentType(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.content(super.mapToJson(product)))
									.andReturn();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_updateProductPrice_withoutName_ok() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(product.getId(), null, new BigDecimal("100.25"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Map<String, Object> jsonObject = (Map<String, Object>) jsonSlurper.parseText(content);
		
		assertEquals("Response status ", 200, status);
		assertTrue("Response body should not be null", content.trim().length() > 0);
		assertEquals("Message ", "Price has been updated", jsonObject.get("message"));
		
		//Rollback
		mvc.perform(MockMvcRequestBuilders.put(uri, id)
										.contentType(MediaType.APPLICATION_JSON)
										.accept(MediaType.APPLICATION_JSON)
										.content(super.mapToJson(product)))
									.andReturn();
	}
	
	@Test
	public void test_updateProductPrice_idNotMatching_NotModified() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("16696652");
		Product product = productService.findProductById(id);
		Product updatedProduct = getProductObject(new BigInteger("123456789"), product.getName(), new BigDecimal("100.25"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 304, status);
	}
	
	@Test
	public void test_updateProductPrice_idNotFound_NotModified() throws Exception {
		String uri = "/api/products/{id}";
		BigInteger id = new BigInteger("123456789");
		Product updatedProduct = getProductObject(new BigInteger("123456789"), null, new BigDecimal("100.25"), "USD");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id)
															.contentType(MediaType.APPLICATION_JSON)
															.accept(MediaType.APPLICATION_JSON)
															.content(super.mapToJson(updatedProduct)))
														.andReturn();
		
		int status = result.getResponse().getStatus();
		
		assertEquals("Response status ", 304, status);
	}

}
