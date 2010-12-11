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

package org.jboss.test.selenium.utils.array;

/**
 * Abstract class providing generic array transformations.
 * 
 * Use overridden method transform(S[] sourceArray) to transform sourceArray to T[] targetArray. Method transform(S[]
 * sourceArray) is implementation of transformation of each item from type S to type T.
 * 
 * Items are transformed to target array as one-to-one preserving order of source.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <S>
 *            Type of source items.
 * @param <T>
 *            Type of target items.
 */
public abstract class ArrayTransform<S, T> {

    private Class<T> tClass;

    /**
     * Constructs ArrayTransform with implementation of transformation predefining tClass like a class of
     * transformation-target array.
     * 
     * @param tClass
     *            class of type T in which should be typed resulting target array
     */
    public ArrayTransform(Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * This method is implementation of transformation each item of sourceArray and type S to item of type T in
     * targetArray.
     * 
     * @param source
     *            transformation object
     * @return transformation result
     */
    public abstract T transformation(S source);

    /**
     * Process transformation of S[] sourceArray with T[] targetArray like return value.
     * 
     * @param sourceArray
     *            array of type S which should be transformed to targetArray of type T
     * @return targetArray of type T after transformation from S[] sourceArray
     */
    @SuppressWarnings("unchecked")
    public T[] transform(S[] sourceArray) {
        T[] targetArray = (T[]) java.lang.reflect.Array.newInstance(tClass, sourceArray.length);
        for (int i = 0; i < sourceArray.length; i++) {
            targetArray[i] = transformation(sourceArray[i]);
        }
        return targetArray;
    }
}
