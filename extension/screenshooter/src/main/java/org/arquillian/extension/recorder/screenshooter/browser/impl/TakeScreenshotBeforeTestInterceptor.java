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

import java.util.List;
import org.arquillian.extension.recorder.DefaultFileNameBuilder;
import org.arquillian.extension.recorder.When;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class TakeScreenshotBeforeTestInterceptor extends AbstractTakeScreenshotInterceptor {

    private boolean alreadyDone = false;

    public TakeScreenshotBeforeTestInterceptor(TakeScreenshot takeScreenshotEvent,
            TakeScreenshotAndReportService takeScreenAndReportService,
            InterceptorRegistry interceptorRegistryService) {
        super(takeScreenshotEvent, takeScreenAndReportService, interceptorRegistryService);
    }

    @Override
    public Object intercept(InvocationContext context) throws Throwable {
        List<Interceptor> previouslyRegistered = interceptorRegistryService.unregisterAll();
        Object result = context.invoke();
        if(previouslyRegistered.isEmpty()) {
            return result;
        }

        if (context.getMethod().getName().equals("get") && !alreadyDone) {
            When when = When.BEFORE;
            takeScreenshotEvent.getMetaData().setOptionalDescription("get");

            DefaultFileNameBuilder nameBuilder = DefaultFileNameBuilder.getInstance();
            String screenshotName = nameBuilder
                    .withMetaData(takeScreenshotEvent.getMetaData())
                    .withStage(when)
                    .withResourceIdentifier(ResourceIdentifierFactory.getResoruceIdentifier(takeScreenshotEvent.getMetaData(), when))
                    .build();
            takeScreenshotEvent.setFileName(screenshotName);

            takeScreenshotAndReport();

            alreadyDone = true;
        }
        interceptorRegistryService.registerAll(previouslyRegistered);
        return result;
    }

    @Override
    public int getPrecedence() {
        return 99;
    }

}
