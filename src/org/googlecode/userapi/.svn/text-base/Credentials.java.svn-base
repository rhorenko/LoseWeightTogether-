package org.googlecode.userapi;

import java.io.Serializable;

public class Credentials implements Serializable {

	private static final long serialVersionUID = 9058889072946466921L;

    private String login;
    private String pass;
    private String remixpass;
    private String sid;
    
    //captcha stuff
    public String captcha_sid;
    public String captcha_decoded;

    public Credentials(String login, String pass, String remixpass) {
        if (login == null || pass == null) throw new IllegalArgumentException("login/pass must not be null");
        this.login = login;
        this.pass = pass;
        this.remixpass = remixpass;
    }

    public Credentials(String login, String pass, String remixpass, String sid) {
        if (login == null || pass == null) throw new IllegalArgumentException("login/pass must not be null");
        this.login = login;
        this.pass = pass;
        this.remixpass = remixpass;
        this.sid = sid;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getRemixpass() {
        return remixpass;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setRemixpass(String remixpass) {
        this.remixpass = remixpass;
    }
}
