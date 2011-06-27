/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.ajocado.junit.ftest;

import static org.jboss.arquillian.ajocado.Ajocado.jq;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveAttribute;
import static org.jboss.arquillian.ajocado.Ajocado.retrieveText;
import static org.jboss.arquillian.ajocado.Ajocado.waitGui;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.waiting.retrievers.AttributeRetriever;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
@RunWith(Arquillian.class)
public class EscapingTestCase extends SampleApplication {

    String paragraphText = "Tester\'s Paragraph";
    String paragraphTitle = "my paragraph's title";

    JQueryLocator paragraph = jq("p:contains('" + paragraphText + "')");
    TextRetriever retrieveParagraphText = retrieveText.locator(paragraph);
    AttributeRetriever retrieveParagraphTitle = retrieveAttribute.attributeLocator(paragraph.getAttribute(Attribute.TITLE));

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return createDeploymentForClass(EscapingTestCase.class);
    }

    @Test
    public void testEscapingTextRetriever() {
        openContext();
        Assert.assertTrue(selenium.isElementPresent(paragraph));
        Assert.assertEquals(waitGui.waitForChangeAndReturn("unknown", retrieveParagraphText), paragraphText);
    }

    @Test
    public void testEscapingAttributeRetriever() {
        openContext();
        Assert.assertTrue(selenium.isElementPresent(paragraph));
        Assert.assertEquals(waitGui.waitForChangeAndReturn("unknown", retrieveParagraphTitle), paragraphTitle);
    }
}
