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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.graphene.spi.page.PageExtension;


/**
 * The implementation of {@link PageExtensionRegistry}
 *
 * @author Lukas Fryc
 */
public class PageExtensionRegistryImpl implements PageExtensionRegistry {

    private Map<String, PageExtension> pageExtensions = new HashMap<String, PageExtension>();

    @Override
    public void register(PageExtension... extensions) {
        for (PageExtension extension: extensions) {
            pageExtensions.put(extension.getName(), extension);
        }
    }

    @Override
    public void register(Collection<PageExtension> extensions) {
        if (extensions == null) {
            throw new IllegalArgumentException("The parameter [extensions] is null.");
        }
        for (PageExtension extension: extensions) {
            pageExtensions.put(extension.getName(), extension);
        }
    }

    @Override
    public PageExtension getExtension(String name) {
        return pageExtensions.get(name);
    }

    @Override
    public Collection<PageExtension> getExtensions() {
        return Collections.unmodifiableCollection(pageExtensions.values());
    }

    @Override
    public void flush() {
        pageExtensions.clear();
    }
}
