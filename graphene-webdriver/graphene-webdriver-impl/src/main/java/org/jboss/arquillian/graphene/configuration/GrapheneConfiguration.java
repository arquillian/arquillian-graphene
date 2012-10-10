/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.configuration;

import java.lang.annotation.Annotation;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.configuration.ConfigurationMapper;
import org.jboss.arquillian.drone.spi.DroneConfiguration;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneConfiguration implements DroneConfiguration<GrapheneConfiguration> {

    private long waitAjaxInterval = 2;

    private long waitGuiInterval = 1;

    private long waitModelInterval = 5;

    private long waitGuardInterval = waitAjaxInterval;

    public long getWaitAjaxInterval() {
        return waitAjaxInterval;
    }

    public long getWaitGuardInterval() {
        return waitGuardInterval;
    }

    public long getWaitGuiInterval() {
        return waitGuiInterval;
    }

    public long getWaitModelInterval() {
        return waitModelInterval;
    }

    public void validate() {
        if (waitAjaxInterval <= 0) {
            throw new IllegalArgumentException("The waitAjaxInterval property has to be a positive number.");
        }
        if (waitGuiInterval <= 0) {
            throw new IllegalArgumentException("The waitGuiInterval property has to be a positive number.");
        }
        if (waitModelInterval <= 0) {
            throw new IllegalArgumentException("The waitModelInterval property has to be a positive number.");
        }
        if (waitGuardInterval <= 0) {
            throw new IllegalArgumentException("The waitGuardInterval property has to be a positive number.");
        }
    }

    @Override
    public String getConfigurationName() {
        return "graphene";
    }

    @Override
    public GrapheneConfiguration configure(ArquillianDescriptor descriptor, Class<? extends Annotation> qualifier) {
        return ConfigurationMapper.fromArquillianDescriptor(descriptor, this, qualifier);
    }

}
