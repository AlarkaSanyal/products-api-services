package org.solution.as.service;

import java.io.IOException;
import java.math.BigInteger;
import org.solution.as.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductService {
	
	Product findProductById(BigInteger id) throws JsonProcessingException, IOException;
	
	boolean updateProductById(Product newProduct);
	
	public void emptyCache();
	
}
