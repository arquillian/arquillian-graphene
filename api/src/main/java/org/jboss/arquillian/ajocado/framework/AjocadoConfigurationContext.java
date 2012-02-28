package org.jboss.arquillian.ajocado.framework;

@Deprecated
public class AjocadoConfigurationContext {
    
    public static void set(AjocadoConfiguration configuration) {
        GrapheneConfigurationContext.set(configuration);
    }
    
    public static AjocadoConfiguration getProxy() {
        return (AjocadoConfiguration) GrapheneConfigurationContext.getProxy();
    }
    
    public static boolean isInitialized() {
        return GrapheneConfigurationContext.isInitialized();
    }
    
}
