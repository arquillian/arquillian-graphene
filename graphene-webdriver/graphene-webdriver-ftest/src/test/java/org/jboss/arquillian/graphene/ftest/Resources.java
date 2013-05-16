/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Resources {

    private final Collection<Resource> resources;
    private final String pkg;

    private Resources(String pkg, Collection<Resource> resources) {
        this.resources = resources;
        this.pkg = pkg;
    }

    public static Resources inCurrentPackage() {
        try {
            String pkg = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getPackage().getName();
            return new Resources(pkg, Collections.EMPTY_LIST);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't load the current package.", e);
        }
    }

    public static Resources inPackage(String pkg) {
        return new Resources(pkg, Collections.EMPTY_LIST);
    }

    public Resources find(String resource) {
        List<Resource> newResources = new ArrayList<Resource>(this.resources);
        newResources.add(Resource.inPackage(pkg).find(resource));
        return new Resources(pkg, newResources);
    }

    public Resources all() {
        List<Resource> newResources = new ArrayList<Resource>(this.resources);
        newResources.addAll(Resource.inPackage(pkg).all());
        return new Resources(pkg, newResources);
    }

    public WebArchive buildWar(String name) {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, name);
        for (Resource resource: resources) {
            archive = archive.addAsWebResource(resource.location, resource.location);
        }
        return archive;
    }

}
