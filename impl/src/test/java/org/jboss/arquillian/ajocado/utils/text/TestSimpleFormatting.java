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
package org.jboss.arquillian.ajocado.utils.text;

import org.testng.Assert;

import org.testng.annotations.Test;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestSimpleFormatting {

    Object[] simpleParameters = new Object[] { 1, "2", '3' };
    Object[] parametrizedParameters = new Object[] { "{}{1}{3}{0}{}{2}", "{1}{}{3}{0}{}{2}", "{3}{1}{}{0}{}{2}" };

    @Test
    public void testSimpleWithoutNumbers() {
        Assert.assertEquals("a1b2c3d", format("a{}b{}c{}d", simpleParameters));
    }

    @Test
    public void testSimpleWithNumbers() {
        Assert.assertEquals("a1b2c3d", format("a{0}b{1}c{2}d", simpleParameters));
    }

    @Test
    public void testSimpleWithNumbersNotInOrder() {
        Assert.assertEquals("a3b1c2d", format("a{2}b{0}c{1}d", simpleParameters));
    }

    @Test
    public void testParametrizedParametersWithoutNumbers() {
        Assert.assertEquals("a{}{1}{3}{0}{}{2}b{1}{}{3}{0}{}{2}c{3}{1}{}{0}{}{2}d",
            format("a{}b{}c{}d", parametrizedParameters));
    }

    @Test
    public void testParametrizedParametersWithNumbers() {
        Assert.assertEquals("a{}{1}{3}{0}{}{2}b{1}{}{3}{0}{}{2}c{3}{1}{}{0}{}{2}d",
            format("a{0}b{1}c{2}d", parametrizedParameters));
    }

    @Test
    public void testParametrizedParametersWithNumbersNotInOrder() {
        Assert.assertEquals("a{3}{1}{}{0}{}{2}b{}{1}{3}{0}{}{2}c{1}{}{3}{0}{}{2}d",
            format("a{2}b{0}c{1}d", parametrizedParameters));
    }
}
