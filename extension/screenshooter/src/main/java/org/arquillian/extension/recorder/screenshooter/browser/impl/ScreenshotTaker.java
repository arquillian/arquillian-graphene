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

import org.arquillian.extension.recorder.DefaultFileNameBuilder;
import org.arquillian.extension.recorder.When;
import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.browser.configuration.BrowserScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.spi.event.suite.After;

import static java.lang.Integer.MIN_VALUE;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ScreenshotTaker {

    @Inject
    private Instance<ScreenshooterConfiguration> configuration;

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Inject
    private Instance<InterceptorRegistry> interceptorRegistry;

    public void onTakeScreenshot(@Observes TakeScreenshot event) {
        if(event.getWhen() == When.BEFORE && configuration.get().getTakeBeforeTest()) {
            TakeScreenshotBeforeTestInterceptor beforeInterceptor =
                    new TakeScreenshotBeforeTestInterceptor(event, getTakeAndReportService(), interceptorRegistry.get());
            interceptorRegistry.get().register(beforeInterceptor);

            boolean willTakeScreenshot = false;

            if(((event.getAnnotation() != null && event.getAnnotation().takeOnEveryAction()))
                    || (event.getAnnotation() == null) && ((BrowserScreenshooterConfiguration) configuration.get()).getTakeOnEveryAction()) {
                willTakeScreenshot = true;
            }

            if (willTakeScreenshot) {
                TakeScreenshotOnEveryActionInterceptor onEveryActionInterceptor =
                        new TakeScreenshotOnEveryActionInterceptor(event, getTakeAndReportService(), interceptorRegistry.get());
                interceptorRegistry.get().register(onEveryActionInterceptor);
            }
        } else {
            DefaultFileNameBuilder nameBuilder = new DefaultFileNameBuilder();
            String screenshotName = nameBuilder
                    .withMetaData(event.getMetaData())
                    .withStage(event.getWhen())
                    .withResourceIdentifier(ResourceIdentifierFactory.getResoruceIdentifier(event.getMetaData(), event.getWhen()))
                    .build();
            event.setFileName(screenshotName);

            getTakeAndReportService().takeScreenshotAndReport(event);
        }
    }

    public void unregisterInterceptors(@Observes(precedence = MIN_VALUE) After event) {
        InterceptorRegistry registry = interceptorRegistry.get();
        registry.unregisterAll();
    }

    private TakeScreenshotAndReportService getTakeAndReportService() {
        return serviceLoader.get().onlyOne(TakeScreenshotAndReportService.class);
    }
}