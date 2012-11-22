/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.graphene.javascript.JavaScriptUtils;
import org.jboss.arquillian.graphene.spi.page.PageExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Provider for {@link PageExtensionInstallator}s using {@link org.openqa.selenium.JavascriptExecutor#executeScript(java.lang.String, java.lang.Object[]) }
 * method.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RemotePageExtensionInstallatorProvider extends AbstractPageExtensionInstallatorProvider {

    private final WebDriver driver;

    public RemotePageExtensionInstallatorProvider(PageExtensionRegistry registry, WebDriver driver) {
        super(registry);
        Validate.notNull(driver);
        this.driver = driver;
        if (!(driver instanceof JavascriptExecutor)) {
            throw new IllegalStateException("Can't use the given driver to execute javascript, because it doesn't implement " + JavascriptExecutor.class + " interface.");
        }
    }

    @Override
    public PageExtensionInstallator createInstallator(final PageExtension extension) {
        return new AbstractPageExtensionInstallator(extension, this) {
            @Override
            protected void installWithoutRequirements() {
                JavaScriptUtils.execute((JavascriptExecutor) driver, extension.getExtensionScript());
            }

            @Override
            public boolean isInstalled() {
                Object result = JavaScriptUtils.execute((JavascriptExecutor) driver, extension.getInstallationDetectionScript());
                if (!(result instanceof Boolean)) {
                    throw new IllegalStateException("The result of installation detection script is not boolean as expected, " + result + " given.");
                }
                return (Boolean) result;
            }
        };
    }

}
