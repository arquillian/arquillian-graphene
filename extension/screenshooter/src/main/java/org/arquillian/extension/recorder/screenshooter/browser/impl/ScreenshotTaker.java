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
import org.arquillian.extension.recorder.screenshooter.Screenshooter;
import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.ScreenshotMetaData;
import org.arquillian.extension.recorder.screenshooter.api.Screenshot;
import org.arquillian.extension.recorder.screenshooter.browser.configuration.BrowserScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.Before;

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

    @Inject
    private Instance<Screenshooter> screenshooter;

    public void registerOnEveryActionInterceptor(@Observes Before event) {
        BrowserScreenshooterConfiguration configuration = (BrowserScreenshooterConfiguration) this.configuration.get();
        Screenshot annotation = event.getTestMethod().getAnnotation(Screenshot.class);

        if (((annotation != null && annotation.takeOnEveryAction()))
            || ((annotation == null) && (configuration.getTakeOnEveryAction()))) {
            registerInterceptor(event);
        }
    }

    public void onTakeScreenshot(@Observes TakeScreenshot event) {
            DefaultFileNameBuilder nameBuilder = new DefaultFileNameBuilder();
            String screenshotName = nameBuilder
                    .withMetaData(event.getMetaData())
                    .withStage(event.getWhen())
                .withResourceIdentifier(
                    ResourceIdentifierFactory.getResoruceIdentifier(event.getMetaData(), event.getWhen()))
                    .build();
            event.setFileName(screenshotName);

            getTakeAndReportService().takeScreenshotAndReport(event);
    }

    public void unregisterInterceptors(@Observes(precedence = MIN_VALUE) After event) {
        InterceptorRegistry registry = interceptorRegistry.get();
        registry.unregisterAll();
    }

    private TakeScreenshotAndReportService getTakeAndReportService() {
        return serviceLoader.get().onlyOne(TakeScreenshotAndReportService.class);
    }

    private ScreenshotMetaData getMetaData(Before event) {
        ScreenshotMetaData metaData = new ScreenshotMetaData();
        metaData.setTestClass(event.getTestClass());
        metaData.setTestMethod(event.getTestMethod());
        metaData.setTimeStamp(System.currentTimeMillis());
        metaData.setResourceType(screenshooter.get().getScreenshotType());
        return metaData;
    }

    private String getScreenshotName(ScreenshotMetaData metaData, When when) {
        DefaultFileNameBuilder nameBuilder = new DefaultFileNameBuilder();
        return nameBuilder
            .withMetaData(metaData)
            .withStage(when)
            .build();
    }

    private void registerInterceptor(Before event) {
        ScreenshotMetaData metaData = getMetaData(event);
        String screenshotName = getScreenshotName(metaData, When.ON_EVERY_ACTION);
        Screenshot annotation = event.getTestMethod().getAnnotation(Screenshot.class);

        TakeScreenshot takeScreenshot = new TakeScreenshot(screenshotName, metaData, When.ON_EVERY_ACTION, annotation);

        TakeScreenshotOnEveryActionInterceptor onEveryActionInterceptor =
            new TakeScreenshotOnEveryActionInterceptor(takeScreenshot, getTakeAndReportService(),
                interceptorRegistry.get());

        interceptorRegistry.get().register(onEveryActionInterceptor);
    }
}