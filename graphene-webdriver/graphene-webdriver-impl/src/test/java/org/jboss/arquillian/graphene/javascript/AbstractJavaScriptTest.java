package org.jboss.arquillian.graphene.javascript;

import java.text.MessageFormat;

public abstract class AbstractJavaScriptTest {

    public String invocation(String base, String method) {
        String call = DefaultExecutionResolver.FUNCTION.replace("\n", "") + MessageFormat.format(DefaultExecutionResolver.CALL, base, method).replace("\n", "");
        return call;
    }
}
