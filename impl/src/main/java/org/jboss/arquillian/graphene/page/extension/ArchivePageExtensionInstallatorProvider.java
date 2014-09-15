/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.page.extension;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.javascript.JavaScriptUtils;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Provider for {@link PageExtensionInstallator}s using {@link org.openqa.selenium.JavascriptExecutor#executeScript(java.lang.String, java.lang.Object[]) }
 * method.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ArchivePageExtensionInstallatorProvider extends AbstractPageExtensionInstallatorProvider {

    private final JavascriptExecutor executor;

    public ArchivePageExtensionInstallatorProvider(PageExtensionRegistry registry, JavascriptExecutor executor) {
        super(registry);
        Validate.notNull(executor, "executor should not be null");
        this.executor = executor;
        if (!(executor instanceof JavascriptExecutor)) {
            throw new IllegalStateException("Can't use the given driver to execute javascript, because it doesn't implement " + JavascriptExecutor.class + " interface.");
        }
    }

    @Override
    public PageExtensionInstallator createInstallator(final PageExtension extension) {
        return new AbstractPageExtensionInstallator(extension, this) {
            @Override
            protected void installWithoutRequirements() {
                JavaScript injectjs = JavaScript.fromResource("injectjs.js");

                String scriptName = extension.getName();
                String scriptUrl = String.format("http://127.0.0.1:8080/__graphene/%s.js", scriptName);

                JavaScriptUtils.execute(executor, injectjs, scriptUrl);
            }

            @Override
            public boolean isInstalled() {
                Object result = JavaScriptUtils.execute(executor, extension.getInstallationDetectionScript());
                if (!(result instanceof Boolean)) {
                    throw new IllegalStateException("The result of installation detection script is not boolean as expected, " + result + " given.");
                }
                return (Boolean) result;
            }
        };
    }

}
