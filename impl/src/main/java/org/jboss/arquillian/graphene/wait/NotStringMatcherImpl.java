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
package org.jboss.arquillian.graphene.wait;

import org.jboss.arquillian.graphene.condition.StringConditionFactory;
import org.jboss.arquillian.graphene.fluent.FluentBase;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class NotStringMatcherImpl<FLUENT> extends AbstractNegatable<StringMatcher<FLUENT>> implements NotStringMatcher<FLUENT> {

    private final StringConditionFactory<StringConditionFactory> factory;
    private final FluentBase<FLUENT> fluentBase;

    public NotStringMatcherImpl(StringConditionFactory<StringConditionFactory> factory, FluentBase<FLUENT> fluentBase) {
        this.factory = factory;
        this.fluentBase = fluentBase;
    }

    @Override
    public FLUENT contains(String expected) {
        return fluentBase.commit(getNegation() ? factory.not().contains(expected) : factory.contains(expected));
    }

    @Override
    public FLUENT equalTo(String expected) {
        return fluentBase.commit(getNegation() ? factory.not().equalTo(expected) : factory.equalTo(expected));
    }

    @Override
    public FLUENT equalToIgnoreCase(String expected) {
        return fluentBase.commit(getNegation() ? factory.not().equalToIgnoreCase(expected) : factory.equalToIgnoreCase(expected));
    }

    @Override
    public FLUENT matches(String expected) {
        return fluentBase.commit(getNegation() ? factory.not().matches(expected) : factory.matches(expected));
    }

    @Override
    protected StringMatcher<FLUENT> copy() {
        return new NotStringMatcherImpl<FLUENT>(factory, fluentBase);
    }

}
