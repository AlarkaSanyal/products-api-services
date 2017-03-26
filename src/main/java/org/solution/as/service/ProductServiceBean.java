package org.solution.as.service;

import java.io.IOException;
import java.math.BigInteger;
import org.solution.as.model.Price;
import org.solution.as.model.Product;
import org.solution.as.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is the main service class.
 *
 */

@Service
@PropertySource(value={"classpath:/config/application.properties"})
public class ProductServiceBean implements ProductService {

	@Autowired
	private PriceRepository priceRepository;
	
	@Value("${external.api.url}")
	private String url;
	
	@Value("${external.api.url.excludes.fields}")
	private String excludeFields;
	
	/**
	 * This method updates (deletes and inserts new) the price of a Product
	 * @param id
	 * @param price
	 */
	private void updatePrice(BigInteger id, Price price) {
		// Delete the existing record
		priceRepository.deletePriceById(id);
		
		// Insert new price
		priceRepository.insertPriceById(id, price.getValue(), price.getCurrency_code());
	}
	
	/**
	 * This method calls an external API to retrieve the name of a product.
	 * @param id
	 * @return name of a product
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private String getProductName(BigInteger id) throws JsonProcessingException, IOException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		String title = null;
		try {
			response = restTemplate.getForEntity(this.url + id + this.excludeFields, String.class);
		} catch (HttpClientErrorException ex) {
			//Do nothing
		}
		if (response != null) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = null, product = null, item = null, description = null;
			root = mapper.readTree(response.getBody());
			
			// Traverse the JSON response to extract the title (product name)
			if (root != null) {
				product = root.path("product");
				if (product != null) {
					item = product.path("item");
					if (item != null) {
						description = item.path("product_description");
						if (description != null) {
							title = description.path("title").textValue();
						}
					}
				}
			}
		}
		return title;
	} 
	
	/**
	 * This method aggregates product data from multiple (price database and external API call) sources
	 * @param price
	 * @return Product object
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private Product getProduct(Price price) throws JsonProcessingException, IOException {
		String productName = getProductName(price.getId());
		return new Product(price.getId(), productName, price);
	}
	
	@Override
	@Cacheable(value="products", key="#id")
	public Product findProductById(BigInteger id) throws JsonProcessingException, IOException {
		Price price = priceRepository.findPriceById(id);
		if (price == null) {
			return null;
		}
		return getProduct(price);
	}

	@Override
	@CachePut(value="products",key="#newProduct.id")
	public boolean updateProductById(Product newProduct) {
		Price existingPrice = priceRepository.findPriceById(newProduct.getId());
		if (existingPrice == null) {
			return false;
		}
		updatePrice(newProduct.getId(), newProduct.getCurrent_price());
		return true;
	}
	
	@Override
	@CacheEvict(value="products", allEntries=true)
	public void emptyCache() {
		
	}

}
