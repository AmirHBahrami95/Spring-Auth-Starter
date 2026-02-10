package com.XX_DOMAIN_NAME.XX_APP_NAME.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;


/***
// XXX big caveat: 

// 1- internal non-user related business level exceptions
// are handled here, even though it might not relate to user's desired action. 
// for instance, if user add's a CarryOffer with a non-existent location id,
// propagated exceptions are caught here without an intermediate service knows
// jackshit about it

// 2- correct errors might not reach user : in above example, all user gets is
// 'bad request', but not 'why'. he will never find out his location is shite
 */
@ControllerAdvice
public class ExpHandler {
	
	private Logger log = LoggerFactory.getLogger(ExpHandler .class);
	
	@ExceptionHandler(BusinessLogicException.class) // custom BusinessLogic erros
	public ResponseEntity<Object> handleServiceException(BusinessLogicException ex) {
		Map<String,String> result=new HashMap<>();	
	  	result.put("code",String.valueOf(((BusinessLogicException)ex).getCode()));
	  	result.put("err",((BusinessLogicException)ex).getUserMessage());
	    return ResponseEntity.status(((BusinessLogicException)ex).getCode()).body(result);
	  }
	
	@ExceptionHandler(org.springframework.dao.DataRetrievalFailureException.class)
	public ResponseEntity<Map<String,String>> handleDataRetrievalFailureException( 
	org.springframework.dao.DataRetrievalFailureException ex){
		return ResponseEntity
  		.status(HttpStatus.NOT_FOUND)
  		.body(makeEntityMap("err.general.not_found", 404));
	}
	
	@ExceptionHandler({org.springframework.dao.DataIntegrityViolationException.class})
	public ResponseEntity<Map<String,String>> handleDataIntegrityViolationException(
	org.springframework.dao.DataIntegrityViolationException ex) {
		log.error("bad request 1 :{}",ex.getMessage());
		return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("err.general.bad_request",400));
	}
	
	@ExceptionHandler({
		org.springframework.dao.InvalidDataAccessApiUsageException.class,
		org.hibernate.TransientPropertyValueException.class
	})
	public ResponseEntity<Map<String,String>> handleBadRequest(Exception ex) {
		log.error("bad request 2 :{}",ex.getMessage());
		return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("err.general.bad_request", 400)); // this is when validation failes
	}
	
	@ExceptionHandler(java.time.format.DateTimeParseException.class)
	public ResponseEntity<Map<String,String>> handleMalformedDateFormat(
			Exception ex) {
		return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("err.general.malformed_date_format", 400));
	}
	
	@ExceptionHandler(org.springframework.http.converter.HttpMessageConversionException.class)
	public ResponseEntity<Map<String,String>> handleMalformedHttp(org.springframework.http.converter.HttpMessageConversionException ex) {
		log.error("ExcpHandler.handleMalformedHttp - 400:"+ex.getMessage());
		ex.printStackTrace();
		return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("err.general.malformed_http", 400));
	}
	
	@ExceptionHandler({org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,org.springframework.http.converter.HttpMessageNotReadableException.class})
	public ResponseEntity<Map<String,String>> handleMalformedHttp(Exception ex) {
		return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("err.general.malformed_data", 400));
	}
	
	@ExceptionHandler({
		jakarta.validation.ConstraintViolationException.class,
		jakarta.validation.ValidationException.class,
	})
	public ResponseEntity<Map<String,String>> handleValex(Exception ex) {
		
		return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("err.general.bad_request", 400));
	}
	
	@ExceptionHandler(org.springframework.transaction.TransactionSystemException.class)
	public ResponseEntity<Map<String,Object>> handleValex(org.springframework.transaction.TransactionSystemException ex) {
		log.error("bad request 3 :{}",ex.getMessage());
		Map<String,Object> m=new HashMap<>();
		Iterator<ConstraintViolation<?>> it= 
				((jakarta.validation.ConstraintViolationException)ex.getRootCause())
					.getConstraintViolations().iterator();
		List<String> errs=new ArrayList<>();
		while(it.hasNext()) {
			errs.add(it.next().getMessage());
		}
		m.put("violations", errs);
		m.put("err", "err.general.bad_request");
		m.put("code", 400);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
	}
	
	// XXX this isn't working when user asks for a fucking favicon (fuck favicon)
	@ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
	public ResponseEntity<Map<String,String>> noHandlerFound(org.springframework.web.servlet.NoHandlerFoundException ex){
		return ResponseEntity.status(404).body(makeEntityMap("err.general.not_found",404));
	}
	
	@ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String,String>> wrongMethod(org.springframework.web.HttpRequestMethodNotSupportedException ex){
		ex.printStackTrace();
		return ResponseEntity.status(405).body(makeEntityMap("err.general.wrong_method",405));
	}
	
	@ExceptionHandler(org.springframework.data.redis.RedisConnectionFailureException.class)
	public ResponseEntity<Map<String,String>> fuckRedis(org.springframework.data.redis.RedisConnectionFailureException ex){
		log.error("unable to connect to redis: {}",ex.getMessage());
		return ResponseEntity.status(500).body(makeEntityMap("err.general.internal_server_error",500));
	}
	
	@ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
	public ResponseEntity<Map<String,String>> fucking403(org.springframework.security.authorization.AuthorizationDeniedException ex){
		log.error("access denied: {}",ex.getMessage());
		return ResponseEntity.status(403).body(makeEntityMap("err.auth.access_denied",403));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,String>> handle(Exception ex) {
		log.error("ExcpHandler.handle - 500:"+ex.getMessage());
		ex.printStackTrace();
		return ResponseEntity
		.status(HttpStatus.BAD_REQUEST)
		.body(makeEntityMap("err.general.internal_server_error", 500));
	}
	
	private Map<String,String> makeEntityMap(String error,int code){
		Map<String,String> m=new HashMap<>();	
	  	m.put("code",String.valueOf(code));
	  	m.put("error",error);
	  	return m;
	}

}
