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

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface ElementBuilder<FLUENT> {

    /**
     * Returns an attribute condition builder which can be used to formulate
     * conditions related to the given attribute.
     */
    AttributeBuilder<FLUENT> attribute(String attribute);

    /**
     * Returns a condition builder for conditions with "is" prefix.
     */
    IsNotElementBuilder<FLUENT> is();

    /**
     * Returns a string matcher for the text inside the element.
     */
    NotStringMatcher<FLUENT> text();

    /**
     * Returns an attribute condition builder for 'value' attribute'
     *
     * @see ElementBuilder#attribute(java.lang.String)
     */
    AttributeBuilder<FLUENT> value();

}
