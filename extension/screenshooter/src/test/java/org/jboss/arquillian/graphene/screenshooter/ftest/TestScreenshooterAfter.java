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
package org.jboss.arquillian.graphene.screenshooter.ftest;


import java.io.File;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.fest.util.Collections;
import org.junit.After;
import org.junit.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestScreenshooterAfter extends AbstractScreenshooterTest {

    private final String TEST_NAME = TestScreenshooterAfter.class.getSimpleName();

    @After
    public void checkScreenAfter() {
        List<String> actualNames = Collections.list(
                new File(SCREEN_DIR + TEST_NAME + "/screenshooter_conf_should_be_used").list());
        actualNames.contains("after.png");
    }

    @Test
    public void screenshooter_conf_should_be_used() {
        assertEquals(fragment.getInnerElement().getText(), "pseudo root");
        make4WebDriverActions();
    }
}