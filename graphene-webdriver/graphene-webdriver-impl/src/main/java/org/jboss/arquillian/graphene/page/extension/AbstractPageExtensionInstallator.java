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

import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractPageExtensionInstallator implements PageExtensionInstallator {

    private final PageExtensionInstallatorProvider installatorProvider;
    private final PageExtension extension;

    public AbstractPageExtensionInstallator(PageExtension extension, PageExtensionInstallatorProvider installatorProvider) {
        Validate.notNull(extension);
        Validate.notNull(installatorProvider);
        this.extension = extension;
        this.installatorProvider = installatorProvider;
    }

    @Override
    public void install() {
        if (isInstalled()) {
            return;
        }
        for (String required: getRequired()) {
            installatorProvider.installator(required, false).install();
        }
        installWithoutRequirements();
        if (!isInstalled()) {
            throw new IllegalStateException("The page extension '"+extension.getClass()+"' can't be installed.");
        }
    }

    @Override
    public JavaScript getExtensionScript() {
        return extension.getExtensionScript();
    }

    @Override
    public JavaScript getInstallationDetectionScript() {
        return extension.getInstallationDetectionScript();
    }

    @Override
    public String getName() {
        return extension.getName();
    }

    @Override
    public Collection<String> getRequired() {
        return extension.getRequired();
    }

    abstract protected void installWithoutRequirements();
}
