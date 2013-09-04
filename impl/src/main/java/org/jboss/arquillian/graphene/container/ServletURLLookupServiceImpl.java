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
package org.jboss.arquillian.graphene.container;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.client.protocol.metadata.Servlet;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;

/**
 * Retrieves contextRoot for the current deployment and method.
 *
 * Taken from Warp's CommandBusOnClient implementation.
 *
 * @author Lukas Fryc
 */
public class ServletURLLookupServiceImpl implements ServletURLLookupService {

    @Inject
    private Instance<ProtocolMetaData> protocolMetadataInst;

    public URL getContextRoot(Method testMethod) {
        ProtocolMetaData protocolMetadata = protocolMetadataInst.get();

        if (protocolMetadata == null) {
            return null;
        }

        Collection<HTTPContext> contexts = protocolMetadata.getContexts(HTTPContext.class);

        HTTPContext context = locateHTTPContext(testMethod, contexts);
        URL servletURL = locateCommandEventBusURI(context);

        return servletURL;
    }

    private HTTPContext locateHTTPContext(Method method, Collection<HTTPContext> contexts) {

        TargetsContainer targetContainer = method.getAnnotation(TargetsContainer.class);
        if (targetContainer != null) {
            String targetName = targetContainer.value();

            for (HTTPContext context : contexts) {
                if (targetName.equals(context.getName())) {
                    return context;
                }
            }
            throw new IllegalArgumentException("Could not determin HTTPContext from ProtocolMetadata for target: " + targetName
                    + ". Verify that the given target name in @" + TargetsContainer.class.getSimpleName()
                    + " match a name returned by the deployment container");
        }
        return contexts.toArray(new HTTPContext[] {})[0];
    }

    private URL locateCommandEventBusURI(HTTPContext context) {
        List<Servlet> contextServlets = context.getServlets();
        if (contextServlets == null) {
            throw new IllegalArgumentException("Could not determine URI for WarpFilter in context " + context
                    + ". There are no Servlets in context.");
        }

        Set<String> contextRoots = new HashSet<String>();
        for (Servlet servlet : contextServlets) {
            contextRoots.add(servlet.getContextRoot());
        }

        if (contextRoots.size() == 1) {
            try {
                URI baseURI = context.getServlets().get(0).getBaseURI();
                return new URL("http", baseURI.getHost(), baseURI.getPort(), baseURI.getPath());
            } catch (MalformedURLException e) {
                throw new RuntimeException("Could not convert Servlet to URI, " + context.getServlets().get(0), e);
            }
        } else {
            try {
                return new URL("http", context.getHost(), context.getPort(), "/");
            } catch (MalformedURLException e) {
                throw new RuntimeException("Could not convert HTTPContext to URI, " + context, e);
            }
        }
    }
}
