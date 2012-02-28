package org.jboss.arquillian.ajocado.framework;

@Deprecated
public final class AjaxSeleniumConfiguration {
    
    public static void set(AjaxSelenium selenium) {
        GrapheneSeleniumContext.set(selenium);
    }
    
    public static AjaxSelenium getProxy() {
        return (AjaxSelenium) GrapheneSeleniumContext.getProxy();
    }
    
    public static boolean isInitialized() {
        return GrapheneSeleniumContext.isInitialized();
    }
}
