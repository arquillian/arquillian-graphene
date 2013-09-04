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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Resource {

    final String location;

    Resource(String location) {
        this.location = location;
        if (Resource.class.getClassLoader().getResource(location) == null) {
            throw new IllegalArgumentException("Can't find the resource " + location + ".");
        }
    }

    public void loadPage(WebDriver browser, URL context) {
        browser.get(context.toString() + location);
    }

    @Override
    public String toString() {
        return location;
    }

    public static ResourceBuilder inCurrentPackage() {
        try {
            String pkg = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getPackage().getName();
            return new ResourceBuilder(pkg);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't load the current package.", e);
        }
    }

    public static ResourceBuilder inPackage(String pkg) {
        return new ResourceBuilder(pkg);
    }

    public static class ResourceBuilder {

        private final String pkg;

        public ResourceBuilder(String pkg) {
            this.pkg = pkg;
        }

        public Resource find(String name) {
            return new Resource(pkg.replace(".", "/") + "/" + name);
        }

        public Collection<Resource> all() {
            Collection<Resource> resources = new ArrayList<Resource>();
            File directory = new File(Resource.class.getClassLoader().getResource(pkg.replace(".", "/")).getFile());
            for (File file: directory.listFiles()) {
                resources.add(new Resource(pkg.replace(".", "/") + "/" + file.getName()));
            }
            return resources;
        }

    }

}
