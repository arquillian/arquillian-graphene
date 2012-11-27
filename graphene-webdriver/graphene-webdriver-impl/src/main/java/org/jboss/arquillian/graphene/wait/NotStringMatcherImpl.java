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

import org.jboss.arquillian.graphene.condition.StringConditionFactory;
import org.jboss.arquillian.graphene.fluent.FluentBase;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class NotStringMatcherImpl<Fluent> extends AbstractNegatable<StringMatcher<Fluent>> implements NotStringMatcher<Fluent> {

    private final StringConditionFactory<StringConditionFactory> factory;
    private final FluentBase<Fluent> fluentBase;

    public NotStringMatcherImpl(StringConditionFactory<StringConditionFactory> factory, FluentBase<Fluent> fluentBase) {
        this.factory = factory;
        this.fluentBase = fluentBase;
    }

    @Override
    public Fluent contains(String expected) {
        return fluentBase.commit(getNegation() ? factory.not().contains(expected) : factory.contains(expected));
    }

    @Override
    public Fluent equalTo(String expected) {
        return fluentBase.commit(getNegation() ? factory.not().equalTo(expected) : factory.equalTo(expected));
    }

    @Override
    protected StringMatcher<Fluent> copy() {
        return new NotStringMatcherImpl<Fluent>(factory, fluentBase);
    }

}
