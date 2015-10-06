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

import java.lang.reflect.Method;
import java.util.List;

import com.google.common.collect.Lists;
import org.arquillian.extension.recorder.DefaultFileNameBuilder;
import org.arquillian.extension.recorder.When;
import org.arquillian.extension.recorder.screenshooter.event.TakeScreenshot;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class TakeScreenshotOnEveryActionInterceptor extends AbstractTakeScreenshotInterceptor {

    private int counter = 0;

    private static final List<Method> WHITE_LIST_WEB_DRIVER_METHODS = getWhiteListWebDriverMethods();

    public TakeScreenshotOnEveryActionInterceptor(TakeScreenshot takeScreenshotEvent,
            TakeScreenshotAndReportService takeScreenAndReportservice,
            InterceptorRegistry interceptorRegistryService) {
        super(takeScreenshotEvent, takeScreenAndReportservice, interceptorRegistryService);
    }

    @Override
    public Object intercept(InvocationContext context) throws Throwable {
        List<Interceptor> previouslyRegistered = interceptorRegistryService.unregisterAll();
        Object result = context.invoke();
        if(previouslyRegistered.isEmpty()) {
            return result;
        }
        Method interceptedMethod = context.getMethod();

        if (isInterceptedMethodAllowed(interceptedMethod)) {
            When when = When.ON_EVERY_ACTION;
            takeScreenshotEvent.getMetaData()
                    .setOptionalDescription(interceptedMethod.getName() + Integer.toString(counter++));

            DefaultFileNameBuilder nameBuilder = new DefaultFileNameBuilder();
            String screenshotName = nameBuilder.withMetaData(takeScreenshotEvent.getMetaData()).withStage(when)
                    .withResourceIdentifier(
                            ResourceIdentifierFactory.getResoruceIdentifier(takeScreenshotEvent.getMetaData(), when)).build();
            takeScreenshotEvent.setFileName(screenshotName);
            takeScreenshotEvent.setWhen(screenshotName.contains("before") ? When.BEFORE : When.ON_EVERY_ACTION);

            takeScreenshotAndReport();
        }
        interceptorRegistryService.registerAll(previouslyRegistered);
        return result;
    }

    private boolean isInterceptedMethodAllowed(Method interceptedMethod) {
        boolean result = false;
        for (Method whiteListMethod : WHITE_LIST_WEB_DRIVER_METHODS) {
            if (methodsEqual(interceptedMethod, whiteListMethod)) {
                result = true;
            }
        }
        return result;
    }

    private static List<Method> getWhiteListWebDriverMethods() {
        List<Method> methods = Lists.newArrayList(WebDriver.class.getMethods());

        // see ARQGRA-482
        try {
            methods.remove(WebDriver.class.getMethod("close"));
            methods.remove(WebDriver.class.getMethod("quit"));
        } catch (NoSuchMethodException e) {
        }

        return methods;
    }

    private static boolean methodsEqual(Method first, Method second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (!first.getName().equals(second.getName())) {
            return false;
        }
        if (first.getParameterTypes().length != second.getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < first.getParameterTypes().length; i++) {
            if (!first.getParameterTypes()[i].equals(second.getParameterTypes()[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getPrecedence() {
        return 100;
    }

}
