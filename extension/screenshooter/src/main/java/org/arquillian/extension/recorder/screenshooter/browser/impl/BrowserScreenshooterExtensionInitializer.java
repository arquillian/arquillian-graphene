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
package org.arquillian.extension.recorder.screenshooter.browser.impl;

import org.arquillian.extension.recorder.screenshooter.Screenshooter;
import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.event.ScreenshooterExtensionConfigured;
import org.arquillian.recorder.reporter.impl.TakenResourceRegister;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class BrowserScreenshooterExtensionInitializer {

    @Inject
    @ApplicationScoped
    private InstanceProducer<Screenshooter> screenshooterProducer;

    @Inject
    @ApplicationScoped
    private InstanceProducer<TakenResourceRegister> takenResourceRegister;

    @Inject
    private Instance<ScreenshooterConfiguration> configuration;

    /**
     * Creates {@link Screenshooter} instance.
     *
     * @param event
     */
    public void onScreenshooterExtensionConfigured(@Observes(precedence = Integer.MIN_VALUE) ScreenshooterExtensionConfigured event) {
        if (takenResourceRegister.get() == null) {
            this.takenResourceRegister.set(new TakenResourceRegister());
        }
        BrowserScreenshooter screenshooter = new BrowserScreenshooter(takenResourceRegister.get());
        screenshooter.init(configuration.get());
        this.screenshooterProducer.set(screenshooter);
    }

}
