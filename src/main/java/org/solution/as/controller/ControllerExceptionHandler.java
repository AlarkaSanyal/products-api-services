package org.solution.as.controller;

import java.util.ArrayList;
import java.util.List;
import org.solution.as.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
	public ResponseEntity<Product> handleErrors(Exception ex) {
		if(ex instanceof MethodArgumentNotValidException) {
			List<FieldError> errors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
			List<String> errorMessage = new ArrayList<String>();
			for (FieldError error : errors) {
				errorMessage.add(error.getField() + " : " + error.getDefaultMessage());
			}
			return new ResponseEntity<Product>(new Product(errorMessage.toString()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Product>(new Product("Only numbers accepted for id or price value"), HttpStatus.BAD_REQUEST);
	}
	
}
