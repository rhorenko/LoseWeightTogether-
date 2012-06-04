package org.googlecode.userapi;

/**
 * Created by Ildar Karimov
 * Date: Nov 18, 2009
 */
public class UserapiLoginException extends Exception {
    private static final String LOGIN_INCORRECT = "-1";
    private static final String CAPTCHA_INCORRECT = "-2";
    private static final String LOGIN_INCORRECT_CAPTCHA_REQUIRED = "-3";
    private static final String LOGIN_INCORRECT_CAPTCHA_NOT_REQUIRED = "-4";

    private ErrorType type;
    private String sid;

    public UserapiLoginException(ErrorType type, String sid) {
        this.type = type;
        this.sid = sid;
    }

    public static UserapiLoginException fromSid(String sid) {
        if (sid.equals(UserapiLoginException.LOGIN_INCORRECT))
            return new UserapiLoginException(ErrorType.LOGIN_INCORRECT, sid);
        if (sid.equals(UserapiLoginException.CAPTCHA_INCORRECT))
            return new UserapiLoginException(ErrorType.CAPTCHA_INCORRECT, sid);
        if (sid.equals(UserapiLoginException.LOGIN_INCORRECT_CAPTCHA_REQUIRED))
            return new UserapiLoginException(ErrorType.LOGIN_INCORRECT_CAPTCHA_REQUIRED, sid);
        if (sid.equals(UserapiLoginException.LOGIN_INCORRECT_CAPTCHA_NOT_REQUIRED))
            return new UserapiLoginException(ErrorType.LOGIN_INCORRECT_CAPTCHA_NOT_REQUIRED, sid);
        return null;
    }

    public ErrorType getType() {
        return type;
    }

    public String getSid() {
        return sid;
    }

    public static enum ErrorType {
        /**Incorrect email or password*/
        LOGIN_INCORRECT,
        /**Incorrect CAPTCHA code while submitting email and password*/
        CAPTCHA_INCORRECT,
        /**Incorrect email or password, next login attempt must be submitted with a CAPTCHA code.*/
        LOGIN_INCORRECT_CAPTCHA_REQUIRED,
        /**Incorrect email or password, CAPTCHA is not required*/
        LOGIN_INCORRECT_CAPTCHA_NOT_REQUIRED
    }

}