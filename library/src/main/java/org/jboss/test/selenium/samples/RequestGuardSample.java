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
package org.jboss.test.selenium.samples;

import java.net.URL;

import org.jboss.test.selenium.AbstractTestCase;
import static org.jboss.test.selenium.locator.LocatorFactory.*;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.locator.*;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.*;

public class RequestGuardSample extends AbstractTestCase {

    final URL initialUrl = null;
    final ElementLocator BUTTON_START = jq("#start");
    final ElementLocator BUTTON_INCREMENT = jq(":button");
    final ElementLocator TEXT_COUNT = id("#count");

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
        selenium.addScript(new JavaScript("..."), null);
    }
}
