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
package org.jboss.arquillian.graphene.wait;

import org.jboss.arquillian.graphene.condition.ElementConditionFactory;
import org.jboss.arquillian.graphene.fluent.FluentBase;
import org.jboss.arquillian.graphene.wait.AttributeBuilder;
import org.jboss.arquillian.graphene.wait.ElementBuilder;
import org.jboss.arquillian.graphene.wait.IsNotElementBuilder;
import org.jboss.arquillian.graphene.wait.NotStringMatcher;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ElementBuilderImpl<FLUENT> extends AbstractNegatable<ElementBuilder> implements ElementBuilder<FLUENT> {

    private final ElementConditionFactory factory;
    private final FluentBase<FLUENT> fluentBase;

    public ElementBuilderImpl(ElementConditionFactory factory, FluentBase<FLUENT> fluentBase) {
        this.factory = factory;
        this.fluentBase = fluentBase;
    }

    @Override
    protected ElementBuilder copy() {
        return new ElementBuilderImpl(factory, fluentBase);
    }

    @Override
    public AttributeBuilder<FLUENT> attribute(String attribute) {
        return new AttributeBuilderImpl<FLUENT>(factory.attribute(attribute), fluentBase);
    }

    @Override
    public IsNotElementBuilder<FLUENT> is() {
        return new IsNotElementBuilderImpl<FLUENT>(factory, fluentBase);
    }

    @Override
    public NotStringMatcher<FLUENT> text() {
        return new NotStringMatcherImpl<FLUENT>(factory.text(), fluentBase);
    }

    @Override
    public AttributeBuilder<FLUENT> value() {
        return attribute("value");
    }

}
