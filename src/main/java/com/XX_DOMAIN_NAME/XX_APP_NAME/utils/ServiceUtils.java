package XX_DOMAIN_NAME.XX_APP_NAME.app.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ServiceUtils {
	
	public static Pageable makePage(String page,String count) {
		return makePage(Integer.valueOf(page),Integer.valueOf(count));
	}
	
	public static Pageable makePage(int page,int count) {
		if(page<=0) page=0;
		if(count !=20 && count!=50) count=50;
		return PageRequest.of(page,count);
	}

}
