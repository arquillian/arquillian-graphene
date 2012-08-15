package org.jboss.arquillian.graphene.javascript;

/**
 * Marks scripts which needs to be uninstalled after each usage (end of each method).
 * 
 * E.g. they may influence behavior of the application.
 * 
 * @author lfryc
 * 
 */
public interface UninstallableJavaScript {
    void uninstall();
}
