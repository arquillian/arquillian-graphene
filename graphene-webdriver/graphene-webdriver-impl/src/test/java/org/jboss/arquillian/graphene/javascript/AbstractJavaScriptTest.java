package org.jboss.arquillian.graphene.javascript;

import java.text.MessageFormat;

public abstract class AbstractJavaScriptTest {
    
    public String invocation(String base, String method) {
        return MessageFormat.format(DefaultExecutionResolver.CALL, base, method);
    }
}
