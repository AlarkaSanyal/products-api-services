package org.solution.as.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.solution.as.annotations.AcceptableCurrencyCode;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Table
@JsonIgnoreProperties(ignoreUnknown = true, value = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price {
	
	@PrimaryKey
	@Digits(integer = 10, fraction = 0, message="Only 10 digit long numbers allowed")
	private BigInteger id;
	
	@NotNull
	@Digits(integer = 100, fraction = 2, message="Currency format accepted only. e.g.: 10, 0.50, 10.50")
	private BigDecimal value;
	
	@NotNull
	@AcceptableCurrencyCode(message = "Currency Code is not supported")
	private String currencycode;
	
	public Price() {
		super();
	}

	public Price(BigInteger id, BigDecimal value, String currencycode) {
		this.id = id;
		this.value = value;
		this.currencycode = currencycode;
	}
	
	public BigInteger getId() {
		return id;
	}
	
	public void setId(BigInteger id) {
		this.id = id;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public String getCurrencycode() {
		return currencycode;
	}
	
	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}
	
}
