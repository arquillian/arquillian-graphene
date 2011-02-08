/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.ajocado.waiting.retrievers;

/**
 * <p>
 * Holds typed value retrieved from page in thread local storage.
 * </p>
 * 
 * @param <T>
 *            the type of object to store
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface RetrievedValueHolder<T> {
    /**
     * Initializes the value associated to this retriever internally.
     */
    void initializeValue();

    /**
     * Initializes the value associated to this retriever with given value.
     * 
     * @param value
     *            the value to associate with this retriever
     */
    void setValue(T value);

    /**
     * Returns the last retrieved value.
     * 
     * @return the last retriever value.
     */
    T getValue();
}
