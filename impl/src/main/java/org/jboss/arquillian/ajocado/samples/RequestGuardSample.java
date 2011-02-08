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
package org.jboss.arquillian.ajocado.samples;

import java.net.URL;

import org.jboss.arquillian.ajocado.AbstractTestCase;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.*;

import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;

import static org.jboss.arquillian.ajocado.guard.request.RequestTypeGuardFactory.*;
import static org.jboss.arquillian.ajocado.encapsulated.JavaScript.js;

/**
 * Sample of guarding request to specific request type.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class RequestGuardSample extends AbstractTestCase {

    static final JQueryLocator BUTTON_START = jq("#start");
    static final JQueryLocator BUTTON_INCREMENT = jq(":button");
    static final IdLocator TEXT_COUNT = id("#count");

    final URL initialUrl = null;

    void usage() {

        // no guards defined
        selenium.open(initialUrl);

        // explicitly define what request guard to use for current interaction
        guardHttp(selenium).click(null);

        // continue to use XHR as defined for selenium object
        selenium.controlKeyDown();
        guardXhr(selenium).click(BUTTON_START);
        selenium.controlKeyUp();

        guardNoRequest(selenium).click(null);

        // this action will not fire any request by default
        selenium.addScript(js("..."));
        
        BUTTON_START.format("test");
    }
}
