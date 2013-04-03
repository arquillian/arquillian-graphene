/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.arquillian.graphene.ftest.drone;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Lukas Fryc
 */
public class AbstractInBrowserTest {
    protected final URL SERVER_URL;
    protected final URL HUB_URL;

    {
        try {
            SERVER_URL = new URL("http://127.0.0.1:4444/");
            HUB_URL = new URL(SERVER_URL, "wd/hub");
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }
}
