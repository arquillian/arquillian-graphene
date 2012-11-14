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
package org.jboss.arquillian.graphene.enricher.annotation;

import java.util.List;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.GraphenePageExtensionsContext;
import org.jboss.arquillian.graphene.page.extension.SizzleJSPageExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ByJQuery extends By {

    private String jquerySelector;

    private JavascriptExecutor executor = GrapheneContext.getProxyForInterfaces(JavascriptExecutor.class);

    public ByJQuery(String jquerySelector) {
        this.jquerySelector = jquerySelector;
    }

    public static ByJQuery jquerySelector(String selector) {
        if(selector == null) {
            throw new IllegalArgumentException("Can not find elements when jquerySelector is null!");
        }
        return new ByJQuery(selector);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WebElement> findElements(SearchContext context) {
        SizzleJSPageExtension pageExtension = new SizzleJSPageExtension();
        GraphenePageExtensionsContext.getRegistryProxy().register(pageExtension);
        GraphenePageExtensionsContext.getInstallatorProviderProxy().installator(pageExtension.getName()).install();

        return (List<WebElement>) executor.executeScript("return window.Sizzle('" + jquerySelector + "')");
    }}
