package org.solution.as.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import org.solution.as.model.Price;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, String> {

	@Query("SELECT * FROM price WHERE id=?0")
	public Price findPriceById(BigInteger id);
	
	@Query("SELECT * FROM price")
	public Collection<Price> findPrices();
	
	@Query("DELETE FROM price WHERE id=?0")
	public void deletePriceById(BigInteger id);
	
	@Query("INSERT INTO price (id, value, currencycode) VALUES (?0, ?1, ?2)")
	public Price insertPriceById(BigInteger id, BigDecimal value, String currencycode);
}
