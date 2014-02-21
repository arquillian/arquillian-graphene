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
package org.jboss.arquillian.graphene.enricher;

import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class InFrameInterceptor implements Interceptor {

    private int indexOfFrame = -1;
    private String nameOrIdOfFrame = null;

    public InFrameInterceptor(String nameOrIdOfFrame) {
        if (nameOrIdOfFrame == null || nameOrIdOfFrame.length() == 0) {
            throw new IllegalArgumentException("nameOrIdOfFrame can not be null or an empty string!");
        }
        this.nameOrIdOfFrame = nameOrIdOfFrame;
    }

    public InFrameInterceptor(int indexOfFrame) {
        if (indexOfFrame < 0) {
            throw new IllegalArgumentException("indexOfFrame can not be less than zero!");
        }
        this.indexOfFrame = indexOfFrame;
    }

    @Override
    public Object intercept(InvocationContext context) throws Throwable {
        WebDriver browser = context.getGrapheneContext().getWebDriver();
        if (indexOfFrame != -1) {
            browser.switchTo().frame(indexOfFrame);
        } else if (nameOrIdOfFrame != null) {
            browser.switchTo().frame(nameOrIdOfFrame);
        }
        Object result = null;
        try {
            result = context.invoke();
        } finally {
            browser.switchTo().defaultContent();
        }
        if (result instanceof GrapheneProxyInstance) {
            ((GrapheneProxyInstance) result).registerInterceptor(this);
        }
        return result;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }
}
