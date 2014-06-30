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


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.arquillian.extension.recorder.screenshooter.api.Screenshot;
import org.junit.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestScreenshooterAnnotation extends AbstractScreenshooterTest {

    private final String TEST_NAME = TestScreenshooterAnnotation.class.getSimpleName();

    @Test
    @Screenshot(takeBeforeTest = true)
    public void screenshooter_conf_should_be_overriden_by_annotation1() {
        assertEquals(fragment.getInnerElement().getText(), "pseudo root");
        make4WebDriverActions();
        checkCreatedSreenshots(TEST_NAME +
                "/screenshooter_conf_should_be_overriden_by_annotation1",
                Arrays.asList("before.png"));
    }

    @Test
    @Screenshot(takeWhenTestFailed = false)
    public void screenshooter_conf_should_be_overriden_by_annotation2() {
        assertEquals(fragment.getInnerElement().getText(), "pseudo root");
        make4WebDriverActions();
        checkCreatedSreenshots(TEST_NAME + "/screenshooter_conf_should_be_overriden_by_annotation2");
    }

    @Test
    public void screenshooter_conf_should_be_used() {
        assertEquals(fragment.getInnerElement().getText(), "pseudo root");
        make4WebDriverActions();
        checkCreatedSreenshots(TEST_NAME +
                "/screenshooter_conf_should_be_used",
                Arrays.asList("before.png", "get3_onEveryAction.png",
                        "getCurrentUrl1_onEveryAction.png", "getPageSource2_onEveryAction.png", "getTitle4_onEveryAction.png"));
    }
}