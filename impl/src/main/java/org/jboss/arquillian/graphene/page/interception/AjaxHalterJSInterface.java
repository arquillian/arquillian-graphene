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
package org.jboss.arquillian.graphene.page.interception;

import org.jboss.arquillian.graphene.request.AjaxState;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JavaScript;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@JavaScript(value = "Graphene.Page.AjaxHalter")
@Dependency(sources = "Graphene.Page.AjaxHalter.js", interfaces = XhrInterception.class)
public abstract class AjaxHalterJSInterface implements InstallableJavaScript {

    public abstract boolean isHandleAvailable();

    public abstract int getCurrentStateId(int handle);

    public abstract int getHandle();

    public abstract void continueTo(int handle, int state);

    public abstract void setEnabled(boolean enabled);

    public abstract boolean isEnabled();

    public void continueTo(int handle, AjaxState state) {
        continueTo(handle, state.getStateId());
    }

    public AjaxState getCurrentState(int handle) {
        int currentStateId = getCurrentStateId(handle);
        return AjaxState.forId(currentStateId);
    }
}
