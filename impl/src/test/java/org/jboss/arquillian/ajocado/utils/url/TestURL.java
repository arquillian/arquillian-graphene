package org.jboss.arquillian.ajocado.utils.url;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.Test;

public class TestURL {
    
    @Test
    public void testURL() throws MalformedURLException {
        String contextRoot = "http://localhost:8080";
        String contextPath = "foo/";
        
        URL rootURL = new URL(contextRoot);
        URL pathURL = new URL(rootURL, contextPath);
        
        System.out.println(rootURL);
        System.out.println(pathURL);
    }

}
