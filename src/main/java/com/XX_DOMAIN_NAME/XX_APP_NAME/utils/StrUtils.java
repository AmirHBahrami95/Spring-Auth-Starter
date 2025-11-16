package XX_DOMAIN_NAME.XX_APP_NAME.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {
	
	public static boolean matchRegex(String regex,String payload) {
		Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(payload);
    return matcher.find();
	}
	
}
