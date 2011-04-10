package org.jboss.arquillian.ajocado;
import org.jboss.arquillian.ajocado.cookie.Cookie;
import org.jboss.arquillian.ajocado.cookie.CookieCreateOptions;
import org.jboss.arquillian.ajocado.cookie.CookieOptions;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;

public class TestCookie {
    public void test() {
        AjaxSelenium selenium = AjaxSeleniumContext.getProxy();
        
        CookieCreateOptions createOptions = CookieOptions.forCreation().domain("sample.com");
        Cookie testCookie = Cookie.createCookie("test", "value");
        
        selenium.createCookie(testCookie, createOptions);
        selenium.deleteCookie(testCookie);
        selenium.deleteCookie(testCookie.getName());
    }
}
