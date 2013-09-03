package org.jboss.arquillian.graphene.javascript;

import org.jboss.arquillian.graphene.context.GrapheneContext;

public interface ExecutionResolver {

    Object execute(GrapheneContext context, JSCall call);
}
