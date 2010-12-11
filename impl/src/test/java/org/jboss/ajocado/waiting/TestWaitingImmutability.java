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
package org.jboss.ajocado.waiting;

import org.jboss.ajocado.waiting.ajax.AjaxWaiting;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestWaitingImmutability {
    @Test
    public void testTimeout() {
        final long timeout = Wait.DEFAULT_TIMEOUT + 1;

        AjaxWaiting waitAjax = Wait.waitAjax();
        assertTrue(waitAjax.getTimeout() != timeout);
        assertNotSame(waitAjax, waitAjax.timeout(timeout));

        waitAjax = waitAjax.timeout(timeout);
        assertEquals(waitAjax.getTimeout(), timeout);
        assertSame(waitAjax, waitAjax.timeout(timeout));
    }
}
