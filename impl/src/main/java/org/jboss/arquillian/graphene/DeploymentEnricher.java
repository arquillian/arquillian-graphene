package org.jboss.arquillian.graphene;

import org.jboss.arquillian.container.spi.client.deployment.Deployment;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentScenario;
import org.jboss.arquillian.container.spi.event.DeployManagedDeployments;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.graphene.context.ExtendedGrapheneContext;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.findby.JQuery;
import org.jboss.arquillian.graphene.findby.JQuerySearchContext;
import org.jboss.arquillian.graphene.guard.RequestGuardImpl;
import org.jboss.arquillian.graphene.javascript.JSInterface;
import org.jboss.arquillian.graphene.page.extension.JavaScriptPageExtension;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.jboss.arquillian.graphene.page.interception.AjaxHalterInterface;
import org.jboss.arquillian.graphene.page.interception.XhrInterception;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;

public class DeploymentEnricher {

    private static final String DEPLOYMENT_NAME = "__graphene.war";

    @Inject
    private Instance<DeploymentScenario> deploymentScenario;

    private static final Class<?>[] CLASSES = new Class<?>[] { XhrInterception.class, RequestGuardImpl.class,
            AjaxHalterInterface.class, JQuery.class, JQuerySearchContext.class };

    public void addGrapheneDeployment(@Observes EventContext<DeployManagedDeployments> eventContext) {

        for (Deployment deployment : deploymentScenario.get().deployments()) {
            if (DEPLOYMENT_NAME.equals(deployment.getDescription().getName())) {
                return;
            }
        }

        WebArchive war = ShrinkWrap.create(WebArchive.class, DEPLOYMENT_NAME);
        war.addManifest();
        addPageExtensions(war);
        deploymentScenario.get().addDeployment(new DeploymentDescription(DEPLOYMENT_NAME, war));

        eventContext.proceed();
    }

    public void addPageExtensions(WebArchive archive) {
        ExtendedGrapheneContext context = (ExtendedGrapheneContext) GrapheneContext.getContextFor(WebDriver.class);
        PageExtensionRegistry registry = context.getPageExtensionRegistry();

        for (Class<?> clazz : CLASSES) {
            JSInterface jsInterface = new JSInterface(clazz);
            registerExtension(registry, jsInterface);
        }

        for (PageExtension extension : registry.getExtensions()) {
            String scriptName = extension.getName() + ".js";
            System.err.println("adding " + scriptName);
            JavaScript script = extension.getExtensionScript();

            archive.addAsWebResource(new StringAsset(script.getSourceCode()), scriptName);
        }
    }

    protected <T> void registerExtension(PageExtensionRegistry registry, JSInterface target) {
        if (target.getName() == null || target.getName().length() == 0) {
            throw new IllegalArgumentException("The extension " + target.getInterface() + "has no mapping.");
        }
        if (registry.getExtension(target.getName()) != null) {
            return;
        }
        JavaScriptPageExtension extension = new JavaScriptPageExtension(target.getInterface());
        registry.register(extension);
        for (JSInterface dependency : target.getJSInterfaceDependencies()) {
            registerExtension(registry, dependency);
        }
    }
}
