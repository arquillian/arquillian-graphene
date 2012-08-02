package org.jboss.arquillian.graphene.javascript;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

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
        return ToStringBuilder.reflectionToString(this);
    }
}
