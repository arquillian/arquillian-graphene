package org.jboss.arquillian.graphene.javascript;

import java.util.Arrays;

public class JSCall {

    private JSMethod method;
    private Object[] arguments;

    public JSCall(JSMethod method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    public JSMethod getMethod() {
        return method;
    }

    public JSTarget getTarget() {
        return method.getTarget();
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "JSCall [arguments=" + Arrays.toString(arguments) + ", method=" + method + "]";
    }
}
