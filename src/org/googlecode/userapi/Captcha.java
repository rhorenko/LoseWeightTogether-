package org.googlecode.userapi;

/**
 * Created by Ildar Karimov
 * Date: Sep 9, 2009
 */
public class Captcha {
	
    public static String wrapCaptcha(String url) {
    	if (Captcha.captcha_sid != null && Captcha.captcha_decoded != null) {
    		url += "&fcsid=" + captcha_sid + "&fccode=" + captcha_decoded;
    		Captcha.captcha_sid = null;
    		Captcha.captcha_decoded = null;
    	}
    	return url;
    }
    
    public static void setCaptchaData(String captcha_sid, String captcha_decoded) {
    	Captcha.captcha_sid = captcha_sid;
    	Captcha.captcha_decoded = captcha_decoded;
    }
	
    private static String captcha_sid;
    private static String captcha_decoded;
}
