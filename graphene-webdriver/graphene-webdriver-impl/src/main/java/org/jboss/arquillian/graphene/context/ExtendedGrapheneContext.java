package org.jboss.arquillian.graphene.context;

import org.jboss.arquillian.graphene.page.extension.PageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;

public abstract class ExtendedGrapheneContext extends GrapheneContext {

    public abstract PageExtensionInstallatorProvider getPageExtensionInstallatorProvider();

    public abstract PageExtensionRegistry getPageExtensionRegistry();

}
