package org.jboss.arquillian.graphene.javascript;

import java.text.MessageFormat;

public abstract class AbstractJavaScriptTest {
    
    public String invocation(String base, String method) {
        String call = DefaultExecutionResolver.FUNCTION + MessageFormat.format(DefaultExecutionResolver.CALL, base, method);
        return call;
    }
}
