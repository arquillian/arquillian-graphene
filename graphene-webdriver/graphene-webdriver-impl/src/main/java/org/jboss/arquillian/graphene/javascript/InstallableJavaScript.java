package org.jboss.arquillian.graphene.javascript;

/**
 * Marks scripts which needs to be installed before they can be started be used.
 * 
 * @author lfryc
 * 
 */
public interface InstallableJavaScript {

    public static final String INSTALL_METHOD = "install";

    void install();
}
