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

import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.ScreenshotMetaData;
import org.arquillian.extension.recorder.screenshooter.event.AfterScreenshotTaken;
import org.arquillian.extension.recorder.screenshooter.event.BeforeScreenshotTaken;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public abstract class AbstractTakeScreenshotInterceptor implements Interceptor {

    protected ScreenshooterConfiguration configuration;
    protected Event<BeforeScreenshotTaken> beforeScreenshotTaken;
    protected Event<AfterScreenshotTaken> afterScreenshotTaken;
    protected Event<TakeScreenshot> takeScreenshot;
    protected ScreenshotMetaData metaData;

    public void registerThis(Object objectToRegisterOn) {
        ((GrapheneProxyInstance) objectToRegisterOn).registerInterceptor(this);
    }

    public void unregisterThis(Object objectToUnregisterFrom) {
        ((GrapheneProxyInstance) objectToUnregisterFrom).unregisterInterceptor(this);
    }

    public void setupThis(ScreenshooterConfiguration configuration, Event<BeforeScreenshotTaken> beforeScreenshotTaken,
        Event<AfterScreenshotTaken> afterScreenshotTaken, Event<TakeScreenshot> takeScreenshot, ScreenshotMetaData metaData) {
        this.configuration = configuration;
        this.beforeScreenshotTaken = beforeScreenshotTaken;
        this.afterScreenshotTaken = afterScreenshotTaken;
        this.takeScreenshot = takeScreenshot;
        this.metaData = metaData;
    }
}