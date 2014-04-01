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
import org.arquillian.extension.recorder.screenshooter.event.AfterScreenshotTaken;
import org.arquillian.extension.recorder.screenshooter.event.BeforeScreenshotTaken;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.graphene.proxy.InvocationContext;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class TakeScreenshotBeforeTestInterceptor extends AbstractTakeScreenshotInterceptor {

    @Override
    public Object intercept(InvocationContext context) throws Throwable {
        Object result = context.invoke();

        if (context.getMethod().getName().equals("get")) {

            When when = When.BEFORE;
            metaData.setOptionalDescription("get");

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
        return result;
    }
}
