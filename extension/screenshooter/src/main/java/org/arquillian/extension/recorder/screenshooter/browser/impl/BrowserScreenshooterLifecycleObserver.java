/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.extension.recorder.screenshooter.browser.impl;

import org.arquillian.extension.recorder.DefaultFileNameBuilder;
import org.arquillian.extension.recorder.When;
import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.ScreenshootingStrategy;
import org.arquillian.extension.recorder.screenshooter.ScreenshotMetaData;
import org.arquillian.extension.recorder.screenshooter.ScreenshotType;
import org.arquillian.extension.recorder.screenshooter.browser.configuration.BrowserScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.event.AfterScreenshotTaken;
import org.arquillian.extension.recorder.screenshooter.event.BeforeScreenshotTaken;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.test.spi.TestResult;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.Before;
import org.jboss.arquillian.test.spi.event.suite.TestEvent;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class BrowserScreenshooterLifecycleObserver {

    @Inject
    private Instance<ScreenshootingStrategy> strategy;

    @Inject
    private Instance<ScreenshooterConfiguration> configuration;

    @Inject
    private Instance<TakeScreenshotOnEveryActionInterceptor> takeScreenshotOnEveryActionInterceptor;

    @Inject
    private Instance<TakeScreenshotBeforeTestInterceptor> takeScreenshotBeforeTestInterceptor;

    @Inject
    private Event<BeforeScreenshotTaken> beforeScreenshotTaken;

    @Inject
    private Event<AfterScreenshotTaken> afterScreenshotTaken;

    @Inject
    private Event<TakeScreenshot> takeScreenshot;

    @Inject
    private Instance<TestResult> testResult;

    @Inject
    private Instance<GrapheneContext> grapheneContext;

    public void beforeObserver(@Observes(precedence = Integer.MIN_VALUE) Before event) {
        ScreenshotMetaData metaData = getMetaData(event);
        AbstractTakeScreenshotInterceptor takeScreenshotInterceptor = null;
        metaData.setResourceType(getScreenshotType());

        WebDriver browser = grapheneContext.get().getWebDriver();

        if (((BrowserScreenshooterConfiguration) configuration.get()).getTakeOnEveryAction()) {
            takeScreenshotInterceptor = takeScreenshotOnEveryActionInterceptor.get();
            takeScreenshotInterceptor.registerThis(browser);
            takeScreenshotInterceptor.setupThis(configuration.get(), beforeScreenshotTaken, afterScreenshotTaken,
                    takeScreenshot, metaData);
        }
        if (configuration.get().getTakeBeforeTest()) {
            takeScreenshotInterceptor = takeScreenshotBeforeTestInterceptor.get();
            takeScreenshotInterceptor.registerThis(browser);
            takeScreenshotInterceptor.setupThis(configuration.get(), beforeScreenshotTaken, afterScreenshotTaken,
                    takeScreenshot, metaData);
        }
    }

    public void afterTest(@Observes(precedence = Integer.MAX_VALUE) After event) {
        TestResult result = testResult.get();
        if (strategy.get().isTakingAction(event, result)) {
            ScreenshotMetaData metaData = getMetaData(event);
            metaData.setTestResult(result);
            metaData.setResourceType(getScreenshotType());

            When when =
                    result.getStatus() == TestResult.Status.FAILED ? When.FAILED : When.AFTER;

            DefaultFileNameBuilder nameBuilder = DefaultFileNameBuilder.getInstance();
            String screenshotName = nameBuilder
                    .withMetaData(metaData)
                    .withStage(when)
                    .withResourceIdentifier(ResourceIdentifierFactory.getResoruceIdentifier(metaData, when))
                    .build();

            beforeScreenshotTaken.fire(new BeforeScreenshotTaken(metaData));

            takeScreenshot.fire(new TakeScreenshot(screenshotName, metaData, when));

            afterScreenshotTaken.fire(new AfterScreenshotTaken(metaData));
        }
    }

    private ScreenshotType getScreenshotType() {
        return ScreenshotType.valueOf(ScreenshotType.class, configuration.get().getScreenshotType().toUpperCase());
    }

    private ScreenshotMetaData getMetaData(TestEvent event) {
        ScreenshotMetaData metaData = new ScreenshotMetaData();

        metaData.setTestClass(event.getTestClass());
        metaData.setTestMethod(event.getTestMethod());
        metaData.setTimeStamp(System.currentTimeMillis());

        return metaData;
    }
}
