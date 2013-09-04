package org.jboss.arquillian.graphene.ftest;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.test.api.ArquillianResource;

public class BackChannel extends URLResourceProvider {

    @Override
    public boolean canProvide(Class<?> type) {
        return super.canProvide(type);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
        URL url = (URL) super.lookup(resource, qualifiers);

        try {
            return new URL(url.getProtocol(), "192.168.15.104", url.getPort(), url.getFile());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

}
