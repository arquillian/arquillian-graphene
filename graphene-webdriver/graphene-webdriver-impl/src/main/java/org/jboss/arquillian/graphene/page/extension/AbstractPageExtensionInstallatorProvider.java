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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractPageExtensionInstallatorProvider implements PageExtensionInstallatorProvider {

    private final PageExtensionRegistry registry;

    public AbstractPageExtensionInstallatorProvider(PageExtensionRegistry registry) {
        Validate.notNull(registry);
        this.registry = registry;
    }

    @Override
    public final PageExtensionInstallator installator(String name) {
        return installator(name, true);
    }

    @Override
    public final PageExtensionInstallator installator(String name, boolean checkRequirements) {
        PageExtension extension = getRegistry().getExtension(name);
        if (extension == null) {
            return null;
        }
        if (checkRequirements) {
            checkRequirements(name);
        }
        return createInstallator(extension);
    }

    protected final PageExtensionRegistry getRegistry() {
        return registry;
    }

    private void checkRequirements(String extensionName) {
        List<String> stack = new ArrayList<String>();
        stack.add(extensionName);
        checkRequirements(stack);
    }

    private void checkRequirements(List<String> stack) {
        PageExtension top = getRegistry().getExtension(stack.get(stack.size()-1));
        if (top == null) {
            throw new IllegalArgumentException("There is no registered extension '"+stack.get(stack.size()-1)+"'.");
        }
        for (String requiredName: top.getRequired()) {
            PageExtension required = getRegistry().getExtension(requiredName);
            for (String onStack: stack) {
                if (required.getName().equals(onStack)) {
                    throw new IllegalStateException("There is a cycle in page extension requirements, DFS stack: " + stack);
                }
            }
            stack.add(required.getName());
            checkRequirements(stack);
        }
        stack.remove(stack.size()-1);
    }

    public abstract PageExtensionInstallator createInstallator(PageExtension extension);

}
