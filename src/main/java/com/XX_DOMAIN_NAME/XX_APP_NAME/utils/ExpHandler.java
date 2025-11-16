package XX_DOMAIN_NAME.XX_APP_NAME.app.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


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
  	result.put("error",((BusinessLogicException)ex).getUserMessage());
    return ResponseEntity.status(((BusinessLogicException)ex).getCode()).body(result);
  }
	
	@ExceptionHandler(org.springframework.dao.DataRetrievalFailureException.class)
  public ResponseEntity<Map<String,String>> handleDataRetrievalFailureException(
  		org.springframework.dao.DataRetrievalFailureException ex)
	{
    return ResponseEntity
  		.status(HttpStatus.NOT_FOUND)
  		.body(makeEntityMap("error.general.not_found", 404));
  }
	
	// TODO inspect the sql code and act accordingly
	@ExceptionHandler({org.springframework.dao.DataIntegrityViolationException.class})
  public ResponseEntity<Map<String,String>> handleDataIntegrityViolationException(
  		org.springframework.dao.DataIntegrityViolationException ex) 
	{
		
		// SQL Error: 1062
		// TODO getCode=(ex.getMessage() | gerp SQL | awk 'BEGIN{fs=","} {print $1}' | awk '{print $3}')
		// if code == 1062; then folan...
		// log.error("HERE WE HAVE: "+ex.getMessage());
    return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("error.general.bad_request",400));
  }
	
	@ExceptionHandler({
		// java.lang.NullPointerException.class, // this motherfucker is there bc @Validation works only at controller lvl (TODO)
		org.springframework.dao.InvalidDataAccessApiUsageException.class,
		org.hibernate.TransientPropertyValueException.class
	})
	public ResponseEntity<Map<String,String>> handleBadRequest(
			Exception ex) {
		// log.error("FUCKING IDIOT: "+ex.getMessage());
    return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("error.general.bad_request", 400)); // this is when validation failes
  }
	
	@ExceptionHandler(java.time.format.DateTimeParseException.class)
	public ResponseEntity<Map<String,String>> handleMalformedDateFormat(
			Exception ex) {
    return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("error.general.malformed_date_format", 400));
  }
	
	@ExceptionHandler(org.springframework.http.converter.HttpMessageConversionException.class)
	public ResponseEntity<Map<String,String>> handleMalformedHttp(org.springframework.http.converter.HttpMessageConversionException ex) {
    return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("error.general.malformed_http", 400));
	}
	
	@ExceptionHandler({org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,org.springframework.http.converter.HttpMessageNotReadableException.class})
	public ResponseEntity<Map<String,String>> handleMalformedHttp(Exception ex) {
		// log.error("malformed_data:"+ex.getMessage());
		// ex.printStackTrace();
    return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap("error.general.malformed_data", 400));
	}
	
	@ExceptionHandler({
		jakarta.validation.ConstraintViolationException.class,
		jakarta.validation.ValidationException.class
	})
	public ResponseEntity<Map<String,String>> handleValex(Exception ex) {
    return ResponseEntity
  		.status(HttpStatus.BAD_REQUEST)
  		.body(makeEntityMap(ex.getMessage(), 400));
	}
	
	@ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,String>> handle(Exception ex) {
		log.error("500:"+ex.getMessage());
		// TODO for production : log the 500, but set the status to 400
    return ResponseEntity
    		.status(HttpStatus.BAD_REQUEST)
    		.body(makeEntityMap("error.general.internal_server_error", 500));
  }
	
	private Map<String,String> makeEntityMap(String error,int code){
		Map<String,String> m=new HashMap<>();	
  	m.put("code",String.valueOf(code)); // TODO comment for production
  	m.put("error",error);
  	return m;
	}

}
