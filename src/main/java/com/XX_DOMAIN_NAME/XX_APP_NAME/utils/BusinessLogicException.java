package XX_DOMAIN_NAME.XX_APP_NAME.utils;

public class BusinessLogicException extends RuntimeException{
	
	private String userMessage;
	private int code; // optional though
	
	public BusinessLogicException(String userMssage,int code) {
		super();
		this.userMessage=userMssage;
		this.code=code;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public int getCode() {
		return code;
	}
	
}
