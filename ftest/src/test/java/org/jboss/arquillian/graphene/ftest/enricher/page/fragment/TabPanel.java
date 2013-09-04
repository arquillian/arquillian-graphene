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
package org.jboss.arquillian.graphene.ftest.enricher.page.fragment;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TabPanel implements SwitchableComponent {

    @FindBy(className = "tabpanel-tab-inact")
    private List<WebElement> tabs;

    @FindByJQuery(".tabpanel-tab:visible")
    private Panel actualTab;

    private SwitchType switchType = SwitchType.CLIENT;

    public enum SwitchType {
        CLIENT,
        AJAX,
        HTTP
    }

    @Override
    public Panel switchTo(int i) {
        switch (switchType) {
            case CLIENT:
                tabs.get(i).click();
                Graphene.waitGui();
                break;
            case AJAX:
                guardAjax(tabs.get(i)).click();
                break;
            case HTTP:
                guardHttp(tabs.get(i)).click();
                break;
        }

        return actualTab;
    }

    public void setSwitchType(SwitchType switchType) {
        this.switchType = switchType;
    }

}
