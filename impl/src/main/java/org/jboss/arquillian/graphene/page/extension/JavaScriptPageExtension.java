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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JSMethod;
import org.jboss.arquillian.graphene.javascript.JSTarget;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class JavaScriptPageExtension implements PageExtension {

    private final JavaScript extensionScript;
    private final JavaScript installationDetectionScript;
    private final Collection<String> required;
    private final JSTarget target;

    public JavaScriptPageExtension(Class<?> clazz) {
        this.target = new JSTarget(clazz);
        if (target.getName() == null || target.getName().length() == 0) {
            throw new IllegalArgumentException("The JavaScript page extension can be created only for class where @JavaScript annotation defines non empty value(). The given class " + clazz + " is not annotation this way.");
        }
        Dependency dependency = clazz.getAnnotation(org.jboss.arquillian.graphene.javascript.Dependency.class);
        if (dependency != null) {
            if (dependency.sources().length == 0 && dependency.interfaces().length != 0) {
                throw new IllegalArgumentException("The JavaScript page extension can't have any interface dependencies when it has no source dependencies. Can't create page extension for " + clazz + ".");
            }
            // load javascript sources
            JavaScript dependencyScript = null;
            for (String source: dependency.sources()) {
                if (dependencyScript == null) {
                    dependencyScript = JavaScript.fromResource(source);
                } else {
                    dependencyScript = dependencyScript.join(JavaScript.fromResource(source));
                }
            }
            // load install() method
            if (target.isInstallable()) {
                JSMethod installMethod = target.getJSMethod(InstallableJavaScript.INSTALL_METHOD);
                String functionCall = target.getName() + "." + installMethod.getName() + "();";
                dependencyScript = dependencyScript.join(JavaScript.fromString(functionCall));
            }
            this.extensionScript = dependencyScript;
            // installation detection
            JavaScript jsInstallationDetection = null;
            StringBuilder builder = new StringBuilder();
            for (String object: target.getName().split("\\.")) {
                if (jsInstallationDetection == null) {
                    jsInstallationDetection = JavaScript.fromString("return (typeof " + object + " != 'undefined')");
                } else {
                    jsInstallationDetection = jsInstallationDetection.append(" && (typeof " + builder.toString() + object + " != 'undefined')");
                }
                builder.append(object).append(".");
            }
            installationDetectionScript = jsInstallationDetection;
            // load dependencies
            Collection<String> dependencies = new ArrayList<String>();
            for (Class<?> dependencyInterface: dependency.interfaces()) {
                org.jboss.arquillian.graphene.javascript.JavaScript jsDependencyAnnoation = dependencyInterface.getAnnotation(org.jboss.arquillian.graphene.javascript.JavaScript.class);
                if (jsDependencyAnnoation == null) {
                    throw new IllegalArgumentException("There is a dependency " + dependencyInterface + " of " + clazz + " which isn't annotated by @JavaScript annoation. The JavaScript page extension can't be created.");
                }
                if (jsDependencyAnnoation.value() == null || jsDependencyAnnoation.value().length() == 0) {
                    throw new IllegalArgumentException("There is a dependency " + dependencyInterface + " of " + clazz + " where @JavaScript annoation doesn't define non empty value(). The JavaScript page extension can't be created.");
                }
                dependencies.add(jsDependencyAnnoation.value());
            }
            this.required = Collections.unmodifiableCollection(dependencies);
        } else {
            this.installationDetectionScript = JavaScript.fromString("return true;");
            this.extensionScript = JavaScript.fromString("return true;");
            this.required = Collections.unmodifiableCollection(Collections.EMPTY_LIST);
        }
    }

    @Override
    public String getName() {
        return target.getName();
    }

    @Override
    public JavaScript getExtensionScript() {
        return extensionScript;
    }

    @Override
    public JavaScript getInstallationDetectionScript() {
        return installationDetectionScript;
    }

    @Override
    public Collection<String> getRequired() {
        return required;
    }

}
